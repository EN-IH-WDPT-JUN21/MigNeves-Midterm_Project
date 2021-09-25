package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.utils.EncryptionUtil;
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
class CheckingRepositoryTest {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    Checking checking1;
    Checking checking2;
    AccountHolder accountHolder;
    AccountHolder secondaryHolder;

    @BeforeEach
    void setUp() {
        accountHolder = new AccountHolder("João Neves", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        secondaryHolder = new AccountHolder("Unkown", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder, secondaryHolder));
        checking1 = new Checking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder);
        checking2 = new Checking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder, secondaryHolder);
        checkingRepository.saveAll(List.of(checking1, checking2));
    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void createChecking_Valid_Created(){
        Checking checking3 = new Checking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), secondaryHolder);
        int checkingRepositoryInitialSize = checkingRepository.findAll().size();
        checkingRepository.save(checking3);
        int checkingRepositoryFinalSize = checkingRepository.findAll().size();
        assertEquals(checkingRepositoryInitialSize + 1, checkingRepositoryFinalSize);
    }

    @Test
    void readChecking_Valid_Read(){
        Optional<Checking> checking = checkingRepository.findById(checking2.getId());
        assertTrue(checking.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")).getAmount(), checking.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(250), Currency.getInstance("EUR")).getAmount(), checking.get().getMinimumBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR")).getAmount(), checking.get().getMonthlyMaintenanceFee().getAmount());
        assertEquals(Status.ACTIVE, checking.get().getStatus());
        assertEquals("João Neves", checking.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", checking.get().getPrimaryOwner().getPassword()));
        assertEquals(30, checking.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", checking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", checking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", checking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", checking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Unkown", checking.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", checking.get().getSecondaryOwner().getPassword()));
        assertEquals(90, checking.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", checking.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", checking.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", checking.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", checking.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateChecking_Valid_Updated(){
        Optional<Checking> checking = checkingRepository.findById(checking1.getId());
        int checkingRepositoryInitialSize = checkingRepository.findAll().size();
        assertTrue(checking.isPresent());
        checking.get().setBalance(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")));
        checking.get().setPrimaryOwner(secondaryHolder);
        checkingRepository.save(checking.get());
        int checkingRepositorySizeAfterUpdate = checkingRepository.findAll().size();
        checking = checkingRepository.findById(checking1.getId());
        assertEquals(checkingRepositoryInitialSize, checkingRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")).getAmount(), checking.get().getBalance().getAmount());
        assertEquals(90, checking.get().getPrimaryOwner().getAge());
        assertEquals("Av. Duarte", checking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", checking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", checking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", checking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteChecking_Valid_Deleted(){
        int checkingRepInitialSize = checkingRepository.findAll().size();
        checkingRepository.deleteById(checking1.getId());
        int checkingRepSizeAfterDelete = checkingRepository.findAll().size();
        assertEquals(checkingRepInitialSize - 1, checkingRepSizeAfterDelete);
        checkingRepository.deleteById(checking2.getId());
        int checkingRepFinalSize = checkingRepository.findAll().size();
        assertEquals(checkingRepSizeAfterDelete - 1, checkingRepFinalSize);
    }
}