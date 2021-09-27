package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Transaction;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class FraudDetectorTest {

    @Autowired
    FraudDetector fraudDetector;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void catchFraud() {
        CreditCard creditCard1 = creditCardRepository.getById("CC_1");
        CreditCard creditCard2 = creditCardRepository.getById("CC_2");
        Transaction transaction1 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), LocalDateTime.of(2003,9,20,1,2));
        Transaction transaction2 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), LocalDateTime.of(2003,9,20,1,3));
        Transaction transaction3 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), LocalDateTime.of(2003,9,20,1,4));
        Transaction transaction4 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), LocalDateTime.of(2003,9,20,1,5));
        Transaction transaction5 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), LocalDateTime.of(2003,9,20,1,6));
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4, transaction5));

        LocalDateTime localDateTime = LocalDateTime.now();

        fraudDetector.catchFraud(localDateTime, creditCard1, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")));
        creditCard1 = creditCardRepository.getById("CC_1");
        assertEquals(creditCard1.getStatus(), Status.ACTIVE);

        Transaction transaction6 = new Transaction(creditCard1, creditCard2, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), localDateTime);
        transactionRepository.save(transaction6);

        CreditCard finalCreditCard = creditCard1;
        Throwable exception = assertThrows(ResponseStatusException.class, () -> fraudDetector.catchFraud(localDateTime.plusSeconds(2), finalCreditCard, new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"))));
        assertEquals("403 FORBIDDEN \"Possible fraud was detected for Account CC_1! The Account has been frozen\"", exception.getMessage());

        CreditCard finalCreditCard1 = creditCard1;
        exception = assertThrows(ResponseStatusException.class, () -> fraudDetector.catchFraud(localDateTime.plusNanos(100), finalCreditCard1, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"))));
        assertEquals("403 FORBIDDEN \"Possible fraud was detected for Account CC_1! The Account has been frozen\"", exception.getMessage());
    }
}