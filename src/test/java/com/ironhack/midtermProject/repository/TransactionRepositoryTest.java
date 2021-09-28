package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    Transaction transaction1;
    Transaction transaction2;
    Transaction transaction3;
    Transaction transaction4;
    CreditCard account1;
    Savings account2;
    AccountHolder accountHolder1;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("John Adams", "12345", LocalDate.of(1990, 10, 12), new Address("King Street", "2000-123", "London", "United Kingdom"));
        accountHolderRepository.save(accountHolder1);

        account1 = new CreditCard(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),
                accountHolder1,
                new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.3));
        creditCardRepository.save(account1);
        account2 = savingsRepository.getById("SA_3");

        transaction1 = new Transaction(account1, account2, new Money(BigDecimal.valueOf(123), Currency.getInstance("EUR")), LocalDateTime.of(2000, 10, 1, 0, 0));
        transaction2 = new Transaction(account1, account2, new Money(BigDecimal.valueOf(15), Currency.getInstance("EUR")), LocalDateTime.of(2000, 10, 1, 5, 0));
        transaction3 = new Transaction(account1, account2, new Money(BigDecimal.valueOf(29), Currency.getInstance("EUR")), LocalDateTime.of(2001, 12, 2, 0, 0));
        transaction4 = new Transaction(account1, account2, new Money(BigDecimal.valueOf(97), Currency.getInstance("EUR")), LocalDateTime.of(2003, 9, 21, 0, 0));
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createThirdPartyTransaction_Valid_Created() {
        Transaction transaction5 = new Transaction(account2, account1, new Money(BigDecimal.valueOf(90), Currency.getInstance("EUR")), LocalDateTime.now());
        long transactionRepositoryInitialSize = transactionRepository.count();
        transactionRepository.save(transaction5);
        long transactionRepositoryFinalSize = transactionRepository.count();
        assertEquals(transactionRepositoryInitialSize + 1, transactionRepositoryFinalSize);
    }

    @Test
    void readThirdPartyTransaction_Valid_Read() {
        Optional<Transaction> transaction = transactionRepository.findById(transaction1.getId());
        assertTrue(transaction.isPresent());
        assertEquals(BigDecimal.valueOf(123).setScale(2, RoundingMode.HALF_EVEN), transaction.get().getTransfer().getAmount());
        assertEquals(account1.getId(), transaction.get().getFromAccount().getId());
        assertEquals(account2.getId(), transaction.get().getToAccount().getId());
        assertEquals(LocalDateTime.of(2000, 10, 1, 0, 0), transaction.get().getTransferDate());
    }

    @Test
    void updateThirdPartyTransaction_Valid_Updated() {
        Optional<Transaction> transaction = transactionRepository.findById(transaction2.getId());
        long transactionRepositoryInitialSize = transactionRepository.count();
        assertTrue(transaction.isPresent());
        transaction.get().setTransfer(new Money(BigDecimal.valueOf(1), Currency.getInstance("EUR")));
        transactionRepository.save(transaction.get());
        long transactionRepositorySizeAfterUpdate = transactionRepository.count();
        transaction = transactionRepository.findById(transaction2.getId());
        assertEquals(transactionRepositoryInitialSize, transactionRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(1), Currency.getInstance("EUR")).getAmount(), transaction.get().getTransfer().getAmount());
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

    @Test
    void getHighestDailyTransactionBeforeDate() {
        List<BigDecimal> transactionList = transactionRepository.getHighestDailyTransactionBeforeDate(account1.getId(), LocalDateTime.now());
        assertEquals(BigDecimal.valueOf(138).setScale(2, RoundingMode.HALF_EVEN), transactionList.get(0));
        assertEquals(BigDecimal.valueOf(97).setScale(2, RoundingMode.HALF_EVEN), transactionList.get(1));
        assertEquals(BigDecimal.valueOf(29).setScale(2, RoundingMode.HALF_EVEN), transactionList.get(2));
    }

    @Test
    void findByFromAccount_IdAndTransferDateAfter() {
        int numberTransactions = transactionRepository.findByFromAccount_IdAndTransferDateAfter(account1.getId(), LocalDateTime.of(2000, 10, 1, 1, 0)).size();
        assertEquals(3, numberTransactions);
    }

    @Test
    void getCountOfTransactionsFromAccountId() {
        int numberTransactions = transactionRepository.getCountOfTransactionsFromAccountId(account1.getId());
        assertEquals(4, numberTransactions);
    }
}