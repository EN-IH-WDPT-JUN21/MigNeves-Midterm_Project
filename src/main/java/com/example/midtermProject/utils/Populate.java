package com.example.midtermProject.utils;

import com.example.midtermProject.dao.*;
import com.example.midtermProject.repository.*;
import com.example.midtermProject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Component
public class Populate {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;


    public void populate(){
        AccountHolder accountHolder1 = new AccountHolder( "John Adams", "12345", 26, new Address("King Street", "2000-123", "London", "United Kingdom"));
        AccountHolder accountHolder2 = new AccountHolder("Sofia Alba","abcde",  32, new Address("Queen Street", "0011-254", "Dublin", "Ireland"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        Checking checking1 = new Checking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder1);
        Checking checking2 = new Checking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder1, accountHolder2);
        checkingRepository.saveAll(List.of(checking1, checking2));
        CreditCard creditCard1 = new CreditCard(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),
                accountHolder1,
                new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.3));
        CreditCard creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(175), Currency.getInstance("EUR")),
                accountHolder1,
                accountHolder2,
                new Money(BigDecimal.valueOf(90), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.5));
        creditCardRepository.saveAll(List.of(creditCard1, creditCard2));
        Savings savings1 = new Savings(new Money(BigDecimal.valueOf(999), Currency.getInstance("EUR")),
                accountHolder1,
                BigDecimal.valueOf(0.3),
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")));
        Savings savings2 = new Savings(new Money(BigDecimal.valueOf(333), Currency.getInstance("EUR")),
                accountHolder1,
                accountHolder2,
                BigDecimal.valueOf(0.9),
                new Money(BigDecimal.valueOf(80), Currency.getInstance("EUR")));
        savingsRepository.saveAll(List.of(savings1, savings2));
        StudentChecking studentChecking1 = new StudentChecking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder1);
        StudentChecking studentChecking2 = new StudentChecking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder1, accountHolder2);
        studentCheckingRepository.saveAll(List.of(studentChecking1, studentChecking2));
    }
}
