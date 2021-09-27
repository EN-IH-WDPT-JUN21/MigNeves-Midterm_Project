package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        "server.error.include-message = always"
})
class ThirdPartyTransactionRepositoryTest {

    @Autowired
    ThirdPartyTransactionRepository transactionRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    ThirdPartyTransaction transaction1;
    ThirdPartyTransaction transaction2;
    ThirdParty thirdParty1;
    ThirdParty thirdParty2;
    Checking account1;
    StudentChecking account2;

    @BeforeEach
    void setUp() {
        thirdParty1 = new ThirdParty("Meredith");
        thirdParty2 = new ThirdParty("Oscar");
        thirdPartyRepository.saveAll(List.of(thirdParty1, thirdParty2));

        account1 = checkingRepository.getById("CH_4");
        account2 = studentCheckingRepository.getById("SC_5");

        transaction1 = new ThirdPartyTransaction(account1, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")), ThirdPartyTransactionType.SEND, thirdParty1);
        transaction2 = new ThirdPartyTransaction(account2, new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")), ThirdPartyTransactionType.RECEIVE, thirdParty2);
        transactionRepository.saveAll(List.of(transaction1, transaction2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createThirdPartyTransaction_Valid_Created() {
        ThirdPartyTransaction transaction3 = new ThirdPartyTransaction(account2, new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), ThirdPartyTransactionType.SEND, thirdParty2);
        long transactionRepositoryInitialSize = transactionRepository.count();
        transactionRepository.save(transaction3);
        long transactionRepositoryFinalSize = transactionRepository.count();
        assertEquals(transactionRepositoryInitialSize + 1, transactionRepositoryFinalSize);
    }

    @Test
    void readThirdPartyTransaction_Valid_Read() {
        Optional<ThirdPartyTransaction> transaction = transactionRepository.findById(transaction1.getId());
        assertTrue(transaction.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")).getAmount(), transaction.get().getTransfer().getAmount());
        assertEquals(account1.getId(), transaction.get().getToAccount().getId());
        assertEquals(ThirdPartyTransactionType.SEND, transaction.get().getTransactionType());
        assertEquals(thirdParty1.getId(), transaction.get().getThirdParty().getId());
    }

    @Test
    void updateThirdPartyTransaction_Valid_Updated() {
        Optional<ThirdPartyTransaction> transaction = transactionRepository.findById(transaction2.getId());
        long transactionRepositoryInitialSize = transactionRepository.count();
        assertTrue(transaction.isPresent());
        transaction.get().setTransactionType(ThirdPartyTransactionType.SEND);
        transactionRepository.save(transaction.get());
        long transactionRepositorySizeAfterUpdate = transactionRepository.count();
        transaction = transactionRepository.findById(transaction2.getId());
        assertEquals(transactionRepositoryInitialSize, transactionRepositorySizeAfterUpdate);
        assertEquals(ThirdPartyTransactionType.SEND, transaction.get().getTransactionType());
    }

    @Test
    void deleteThirdPartyTransaction_Valid_Deleted() {
        long transactionRepInitialSize = transactionRepository.count();
        transactionRepository.deleteById(transaction1.getId());
        long transactionRepSizeAfterDelete = transactionRepository.count();
        assertEquals(transactionRepInitialSize - 1, transactionRepSizeAfterDelete);
        transactionRepository.deleteById(transaction2.getId());
        long transactionRepFinalSize = transactionRepository.count();
        assertEquals(transactionRepSizeAfterDelete - 1, transactionRepFinalSize);
    }
}