package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
class SavingsRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    SavingsRepository savingsRepository;

    AccountHolder accountHolder1;
    AccountHolder accountHolder2;
    Savings savings1;
    Savings savings2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("Kelly", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        accountHolder2 = new AccountHolder("Angela", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        savings1 = new Savings(new Money(BigDecimal.valueOf(999), Currency.getInstance("EUR")),
                accountHolder1,
                BigDecimal.valueOf(0.3),
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")));
        savings2 = new Savings(new Money(BigDecimal.valueOf(333), Currency.getInstance("EUR")),
                accountHolder1,
                accountHolder2,
                BigDecimal.valueOf(0.9),
                new Money(BigDecimal.valueOf(80), Currency.getInstance("EUR")));
        savingsRepository.saveAll(List.of(savings1, savings2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createSavingsCard_Valid_Created() {
        Savings savings3 = new Savings(new Money(BigDecimal.valueOf(400), Currency.getInstance("EUR")),
                accountHolder1,
                BigDecimal.valueOf(0.4),
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")));
        long savingsRepositoryInitialSize = savingsRepository.count();
        savingsRepository.save(savings3);
        long savingsRepositoryFinalSize = savingsRepository.count();
        assertEquals(savingsRepositoryInitialSize + 1, savingsRepositoryFinalSize);
    }

    @Test
    void readSavings_Valid_Read() {
        Optional<Savings> savings = savingsRepository.findById(savings2.getId());
        assertTrue(savings.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(333), Currency.getInstance("EUR")).getAmount(), savings.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")).getAmount(), savings.get().getMinimumBalance().getAmount());
        assertEquals(BigDecimal.valueOf(0.5).setScale(4, RoundingMode.HALF_EVEN), savings.get().getInterestRate());
        assertEquals("Kelly", savings.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", savings.get().getPrimaryOwner().getPassword()));
        assertEquals(30, savings.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", savings.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", savings.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", savings.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Angela", savings.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", savings.get().getSecondaryOwner().getPassword()));
        assertEquals(90, savings.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", savings.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", savings.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", savings.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateSavings_Valid_Updated() {
        Optional<Savings> savings = savingsRepository.findById(savings1.getId());
        long creditCardRepositoryInitialSize = savingsRepository.count();
        assertTrue(savings.isPresent());
        savings.get().setMinimumBalance(new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")));
        savings.get().setInterestRate(BigDecimal.valueOf(0.05));
        savingsRepository.save(savings.get());
        long savingsRepositorySizeAfterUpdate = savingsRepository.count();
        savings = savingsRepository.findById(savings1.getId());
        assertEquals(creditCardRepositoryInitialSize, savingsRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(999), Currency.getInstance("EUR")).getAmount(), savings.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")).getAmount(), savings.get().getMinimumBalance().getAmount());
        assertEquals(BigDecimal.valueOf(0.05).setScale(4, RoundingMode.HALF_EVEN), savings.get().getInterestRate());
        assertEquals("Kelly", savings.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", savings.get().getPrimaryOwner().getPassword()));
        assertEquals(30, savings.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", savings.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", savings.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", savings.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteSavings_Valid_Deleted() {
        long creditCardRepInitialSize = savingsRepository.count();
        savingsRepository.deleteById(savings1.getId());
        long creditCardRepSizeAfterDelete = savingsRepository.count();
        assertEquals(creditCardRepInitialSize - 1, creditCardRepSizeAfterDelete);
        savingsRepository.deleteById(savings2.getId());
        long creditCardRepFinalSize = savingsRepository.count();
        assertEquals(creditCardRepSizeAfterDelete - 1, creditCardRepFinalSize);
    }

    @Test
    void findByPrimaryOwner() {
        int savingsOwnedByKelly = savingsRepository.findByPrimaryOwner(accountHolder1.getId()).size();
        assertEquals(2, savingsOwnedByKelly);
    }

    @Test
    void findBySecondaryOwner() {
        int savingsCoOwnedByAngela = savingsRepository.findBySecondaryOwner(accountHolder2.getId()).size();
        assertEquals(1, savingsCoOwnedByAngela);
    }
}