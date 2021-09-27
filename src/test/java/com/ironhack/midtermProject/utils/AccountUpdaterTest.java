package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.repository.SavingsRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)   // Resets DB and id generation (slower)
@TestPropertySource(properties = {      // For testing it uses a "datalayer_tests" database and the same user
        "spring.datasource.url=jdbc:mysql://localhost:3306/banking_test",
        "spring.datasource.username=ironhacker",
        "spring.datasource.password=1r0nh4ck3r",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.defer-datasource-initialization=create-update",
        "spring.datasource.initialization-mode=always",
        "spring.jpa.show-sql=true",
        "server.error.include-message = always",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
})
class AccountUpdaterTest {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountUpdater accountUpdater;

    @Test
    void updateAccounts() {
        CreditCard creditCard = creditCardRepository.getById("CC_1");
        Savings savings = savingsRepository.getById("SA_3");
        Checking checking = checkingRepository.getById("CH_4");

        Money balance = new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"));

        creditCard.setBalance(balance);     // creation date -> 2000-01-01
        savings.setBalance(balance);        // creation date -> 2015-05-26
        checking.setBalance(balance);       // creation date -> 2019-12-30

        LocalDateTime currentTime = LocalDateTime.now();

        creditCard.setLastUpdateDate(creditCard.getCreationDate().withYear(currentTime.getYear() - 1)
                .withMonth(currentTime.getMonthValue())
                .withDayOfMonth(currentTime.getDayOfMonth()));

        savings.setLastUpdateDate(creditCard.getCreationDate().withYear(currentTime.getYear() - 5)
                .withMonth(currentTime.getMonthValue())
                .withDayOfMonth(currentTime.getDayOfMonth()));

        checking.setLastUpdateDate(creditCard.getCreationDate().withYear(currentTime.getYear() - 2)
                .withMonth(currentTime.getMonthValue())
                .withDayOfMonth(currentTime.getDayOfMonth()));

        creditCardRepository.save(creditCard);
        savingsRepository.save(savings);
        checkingRepository.save(checking);

        accountUpdater.updateAccounts();

        creditCard = creditCardRepository.getById("CC_1");
        savings = savingsRepository.getById("SA_3");
        checking = checkingRepository.getById("CH_4");

        assertEquals(BigDecimal.valueOf(1219.87).setScale(2, RoundingMode.HALF_EVEN), creditCard.getBalance().getAmount());  // -> 12 months with interestRate 0.2
        assertEquals(BigDecimal.valueOf(5378.24).setScale(2, RoundingMode.HALF_EVEN), savings.getBalance().getAmount());    // -> 5 years with interestRate 0.4
        assertEquals(BigDecimal.valueOf(712).setScale(2, RoundingMode.HALF_EVEN), checking.getBalance().getAmount());       // -> 2 years with monthlyPenaltyFee of 12
    }
}