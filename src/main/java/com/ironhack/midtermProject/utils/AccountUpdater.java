package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.repository.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Component
public class AccountUpdater {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Transactional
    public void updateAccounts() {
        // Get all Savings, CreditCard and Checking Accounts to update
        List<Savings> savingsList = savingsRepository.findAll();
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        List<Checking> checkingList = checkingRepository.findAll();

        int times;
        LocalDate currentTime = LocalDate.now();

        // For each Savings Account calculate number of years to apply interestRate and update balance
        for (Savings savings : savingsList) {
            times = yearsPassed(savings.getCreationDate(), savings.getLastUpdateDate(), currentTime);
            BigDecimal interest = applyInterestRateXTimes(new Money(savings.getBalance().getAmount(), savings.getBalance().getCurrency())
                    , times, savings.getInterestRate());
            savings.increaseBalance(new Money(interest, Currency.getInstance("EUR")));
            savings.setLastUpdateDate(currentTime);
        }

        // For each CreditCard Account calculate number of months to apply interestRate and update balance
        for (CreditCard creditCard : creditCardList) {
            if (creditCard.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
                times = monthsPassed(creditCard.getCreationDate(), creditCard.getLastUpdateDate(), currentTime);
                BigDecimal interest = applyInterestRateXTimes(new Money(creditCard.getBalance().getAmount(),
                                creditCard.getBalance().getCurrency()), times,
                        creditCard.getInterestRate().divide(BigDecimal.valueOf(12), RoundingMode.HALF_EVEN));
                creditCard.increaseBalance(new Money(interest, Currency.getInstance("EUR")));
            }
            creditCard.setLastUpdateDate(currentTime);
        }

        // For each Checking Account calculate number of years to apply monthlyMaintenanceFee and update balance
        for (Checking checking : checkingList) {
            times = monthsPassed(checking.getCreationDate(), checking.getLastUpdateDate(), currentTime);
            BigDecimal maintenanceFee = checking.getMonthlyMaintenanceFee().getAmount().multiply(BigDecimal.valueOf(times));
            checking.decreaseBalance(new Money(maintenanceFee, Currency.getInstance("EUR")));
            checking.setLastUpdateDate(currentTime);
        }
    }

    private int yearsPassed(LocalDate creationTime, LocalDate lastUpdateTime, LocalDate currentTime) {
        // Calculate number of years already accounted since the Account creation
        int yearsAccounted = lastUpdateTime.getYear() - creationTime.getYear();
        lastUpdateTime = lastUpdateTime.minusYears(yearsAccounted);
        if (creationTime.isAfter(lastUpdateTime)) {
            yearsAccounted--;
        }

        // Calculate number of years passed since the Account creation
        int totalYears = currentTime.getYear() - creationTime.getYear();
        creationTime = creationTime.plusYears(totalYears);
        if (creationTime.isAfter(currentTime)) {
            totalYears--;
        }

        // Return number of years to update
        return totalYears - yearsAccounted;
    }

    private int monthsPassed(LocalDate creationTime, LocalDate lastUpdateTime, LocalDate currentTime) {
        // Calculate number of months since last update
        int monthsPassed = (currentTime.getYear() - lastUpdateTime.getYear()) * 12;
        monthsPassed += currentTime.getMonthValue() - lastUpdateTime.getMonthValue();
        if (creationTime.getDayOfMonth() > lastUpdateTime.getDayOfMonth()) {
            monthsPassed++;
        }
        if (currentTime.getDayOfMonth() < creationTime.getDayOfMonth()) {
            monthsPassed--;
        }
        return monthsPassed;
    }

    private BigDecimal applyInterestRateXTimes(Money balance, int x, BigDecimal interestRate) {
        BigDecimal interest;
        BigDecimal totalInterest = BigDecimal.valueOf(0);
        // Apply interest for every month/year and return sum of interests
        for (int i = 0; i < x; i++) {
            interest = balance.getAmount().multiply(interestRate);
            balance.increaseAmount(interest);
            totalInterest = totalInterest.add(interest);
        }
        return totalInterest;
    }
}
