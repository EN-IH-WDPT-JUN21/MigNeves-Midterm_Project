package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CheckingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

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
class EncryptionUtilTest {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Test
    void matches() {
        String plainPassword1 = "doggy";
        String plainPassword2 = "Jotaro";
        String plainPassword3 = "Steins Gate";

        String encryptedPassword1 = EncryptionUtil.encryptedPassword(plainPassword1);
        String encryptedPassword2 = EncryptionUtil.encryptedPassword(plainPassword2);
        String encryptedPassword3 = EncryptionUtil.encryptedPassword(plainPassword3);

        assertTrue(EncryptionUtil.matches(plainPassword1, encryptedPassword1));
        assertTrue(EncryptionUtil.matches(plainPassword2, encryptedPassword2));
        assertTrue(EncryptionUtil.matches(plainPassword3, encryptedPassword3));
        assertFalse(EncryptionUtil.matches(plainPassword1, encryptedPassword2));
        assertFalse(EncryptionUtil.matches(plainPassword2, encryptedPassword3));
        assertFalse(EncryptionUtil.matches(plainPassword3, encryptedPassword1));
        assertFalse(EncryptionUtil.matches(plainPassword1, encryptedPassword3));
        assertFalse(EncryptionUtil.matches(plainPassword2, encryptedPassword1));
        assertFalse(EncryptionUtil.matches(plainPassword3, encryptedPassword2));
    }

    @Test
    void getSecretKey() {
        AccountHolder accountHolder = accountHolderRepository.getById(1L);
        Money balance = new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR"));
        Checking checking1 = new Checking(balance, accountHolder);
        Checking checking2 = new Checking(balance, accountHolder);
        checkingRepository.saveAll(List.of(checking1, checking2));
        assertNotEquals(checking1.getSecretKey(), checking2.getSecretKey());
    }
}