package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Address;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SavingsRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    SavingsRepository savingsRepository;

    AccountHolder accountHolder;
    AccountHolder secondaryHolder;
    Savings savings1;
    Savings savings2;

    @BeforeEach
    void setUp() {
        accountHolder = new AccountHolder("João Neves", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        secondaryHolder = new AccountHolder("Unkown", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder, secondaryHolder));
        savings1 = new Savings(new Money(BigDecimal.valueOf(999), Currency.getInstance("EUR")),
                accountHolder,
                new Money(BigDecimal.valueOf(0.3), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")));
        savings2 = new Savings(new Money(BigDecimal.valueOf(333), Currency.getInstance("EUR")),
                accountHolder,
                secondaryHolder,
                new Money(BigDecimal.valueOf(0.9), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(80), Currency.getInstance("EUR")));
        savingsRepository.saveAll(List.of(savings1, savings2));
    }

    @AfterEach
    void tearDown() {
        savingsRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void createSavingsCard_Valid_Created(){
        Savings savings3 = new Savings(new Money(BigDecimal.valueOf(400), Currency.getInstance("EUR")),
                accountHolder,
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(0.4), Currency.getInstance("EUR")));
        int savingsRepositoryInitialSize = savingsRepository.findAll().size();
        savingsRepository.save(savings3);
        int savingsRepositoryFinalSize = savingsRepository.findAll().size();
        assertEquals(savingsRepositoryInitialSize + 1, savingsRepositoryFinalSize);
    }

    @Test
    void readSavings_Valid_Read(){
        Optional<Savings> savings = savingsRepository.findById(savings2.getId());
        assertTrue(savings.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(333), Currency.getInstance("EUR")).getAmount(), savings.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")).getAmount(), savings.get().getMinimumBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(0.5), Currency.getInstance("EUR")).getAmount(), savings.get().getInterestRate().getAmount());
        assertEquals("João Neves", savings.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", savings.get().getPrimaryOwner().getPassword()));
        assertEquals(30, savings.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", savings.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", savings.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", savings.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Unkown", savings.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", savings.get().getSecondaryOwner().getPassword()));
        assertEquals(90, savings.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", savings.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", savings.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", savings.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateSavings_Valid_Updated(){
        Optional<Savings> savings = savingsRepository.findById(savings1.getId());
        int creditCardRepositoryInitialSize = savingsRepository.findAll().size();
        assertTrue(savings.isPresent());
        savings.get().setMinimumBalance(new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")));
        savings.get().setInterestRate(new Money(BigDecimal.valueOf(0.05), Currency.getInstance("EUR")));
        savingsRepository.save(savings.get());
        int savingsRepositorySizeAfterUpdate = savingsRepository.findAll().size();
        savings = savingsRepository.findById(savings1.getId());
        assertEquals(creditCardRepositoryInitialSize, savingsRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(999), Currency.getInstance("EUR")).getAmount(), savings.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")).getAmount(), savings.get().getMinimumBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(0.05), Currency.getInstance("EUR")).getAmount(), savings.get().getInterestRate().getAmount());
        assertEquals("João Neves", savings.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", savings.get().getPrimaryOwner().getPassword()));
        assertEquals(30, savings.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", savings.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", savings.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", savings.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", savings.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteSavings_Valid_Deleted(){
        int creditCardRepInitialSize = savingsRepository.findAll().size();
        savingsRepository.deleteById(savings1.getId());
        int creditCardRepSizeAfterDelete = savingsRepository.findAll().size();
        assertEquals(creditCardRepInitialSize - 1, creditCardRepSizeAfterDelete);
        savingsRepository.deleteById(savings2.getId());
        int creditCardRepFinalSize = savingsRepository.findAll().size();
        assertEquals(creditCardRepSizeAfterDelete - 1, creditCardRepFinalSize);
    }
}