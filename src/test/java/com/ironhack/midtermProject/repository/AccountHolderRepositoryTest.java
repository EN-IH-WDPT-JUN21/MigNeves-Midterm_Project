package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountHolderRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder( "John Adams", "12345", 26, new Address("King Street", "2000-123", "London", "United Kingdom"));
        accountHolder2 = new AccountHolder("Sofia Alba","abcde",  32, new Address("Queen Street", "0011-254", "Dublin", "Ireland"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
    }

    @Test
    void createAccountHolder_Valid_Created(){
        AccountHolder accountHolder3 = new AccountHolder( "Louis Smith", "mathsIsFun",21, new Address("Prince Street", "ABC-123", "Mexico City", "Mexico"), new Address("Av. de Segovia", "ZZZ-ZZZ", "Valladolid", "Spain"));
        int accountHolderRepositoryInitialSize = accountHolderRepository.findAll().size();
        accountHolderRepository.save(accountHolder3);
        int accountHolderRepositoryFinalSize = accountHolderRepository.findAll().size();
        assertEquals(accountHolderRepositoryInitialSize + 1, accountHolderRepositoryFinalSize);
    }

    @Test
    void readAccountHolder_Valid_Read(){
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(accountHolder1.getId());
        assertTrue(accountHolder.isPresent());
        assertEquals("John Adams", accountHolder.get().getName());
        Assertions.assertTrue(EncryptionUtil.matches("12345", accountHolder.get().getPassword()));
        assertEquals(26, accountHolder.get().getAge());
        assertEquals("King Street", accountHolder.get().getPrimaryAddress().getAddress());
        assertEquals("2000-123", accountHolder.get().getPrimaryAddress().getPostalCode());
        assertEquals("London", accountHolder.get().getPrimaryAddress().getCity());
        assertEquals("United Kingdom", accountHolder.get().getPrimaryAddress().getCountry());
    }

    @Test
    void updateAccountHolder_Valid_Updated(){
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(accountHolder2.getId());
        int accountRepositoryInitialSize = accountHolderRepository.findAll().size();
        assertTrue(accountHolder.isPresent());
        accountHolder.get().setMailingAddress(new Address("Rua dos Patos", "3123-123", "Leiria", "Portugal"));
        accountHolderRepository.save(accountHolder.get());
        int accountRepositorySizeAfterUpdate = accountHolderRepository.findAll().size();
        accountHolder = accountHolderRepository.findById(accountHolder2.getId());
        assertEquals(accountRepositoryInitialSize, accountRepositorySizeAfterUpdate);
        assertEquals("Rua dos Patos", accountHolder.get().getMailingAddress().getAddress());
        assertEquals("3123-123", accountHolder.get().getMailingAddress().getPostalCode());
        assertEquals("Leiria", accountHolder.get().getMailingAddress().getCity());
        assertEquals("Portugal", accountHolder.get().getMailingAddress().getCountry());
    }

    @Test
    void deleteAccountHolder_Valid_Deleted(){
        int accountHolderRepInitialSize = accountHolderRepository.findAll().size();
        accountHolderRepository.deleteById(accountHolder1.getId());
        int accountHolderRepSizeAfterDelete = accountHolderRepository.findAll().size();
        assertEquals(accountHolderRepInitialSize - 1, accountHolderRepSizeAfterDelete);
        accountHolderRepository.deleteById(accountHolder2.getId());
        int accountHolderRepFinalSize = accountHolderRepository.findAll().size();
        assertEquals(accountHolderRepSizeAfterDelete - 1, accountHolderRepFinalSize);
    }
}