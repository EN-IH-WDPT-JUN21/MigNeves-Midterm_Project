package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class CustomPrefixedIdGeneratorTest {

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

    CreditCard creditCard;
    Savings savings;
    Checking checking;
    StudentChecking studentChecking;
    Address address;
    AccountHolder accountHolder;
    AccountHolder youngAccountHolder;
    Money balance;

    @BeforeEach
    void setUp() {
        address = new Address("Some street", "Portland", "UK", "000-111");
        balance = new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR"));
        accountHolder = new AccountHolder("John", "pass", LocalDate.of(1980, 1, 20), address);
        youngAccountHolder = new AccountHolder("Jack", "pass", LocalDate.of(2003, 12, 12), address);
        accountHolderRepository.saveAll(List.of(accountHolder, youngAccountHolder));
        creditCard = new CreditCard(balance, accountHolder);
        creditCardRepository.save(creditCard);
        savings = new Savings(balance, accountHolder);
        savingsRepository.save(savings);
        checking = new Checking(balance, accountHolder);
        checkingRepository.save(checking);
        studentChecking = new StudentChecking(balance, youngAccountHolder);
        studentCheckingRepository.save(studentChecking);
    }

    @AfterEach
    void tearDown() {
        String creditCardId = creditCard.getId();
        String savingsId = savings.getId();
        String checkingId = checking.getId();
        String studentCheckingId = studentChecking.getId();
        assertEquals("CC", creditCardId.substring(0, 2));
        assertEquals(Integer.parseInt(creditCardId.substring(3)) + 1, Integer.parseInt(savingsId.substring(3)));
        assertEquals("SA", savingsId.substring(0, 2));
        assertEquals(Integer.parseInt(savingsId.substring(3)) + 1, Integer.parseInt(checkingId.substring(3)));
        assertEquals("CH", checkingId.substring(0, 2));
        assertEquals(Integer.parseInt(checkingId.substring(3)) + 1, Integer.parseInt(studentCheckingId.substring(3)));
        assertEquals("SC", studentCheckingId.substring(0, 2));
    }

    @Test
    void createNewId() {
    }

}