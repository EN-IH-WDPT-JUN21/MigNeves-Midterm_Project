package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
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
class AccountHolderRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("John Adams", "12345", LocalDate.of(1970, 1, 1), new Address("King Street", "2000-123", "London", "United Kingdom"));
        accountHolder2 = new AccountHolder("Sofia Alba", "abcde", LocalDate.of(1965, 2, 30), new Address("Queen Street", "0011-254", "Dublin", "Ireland"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAccountHolder_Valid_Created() {
        AccountHolder accountHolder3 = new AccountHolder("Louis Smith", "mathsIsFun", LocalDate.of(1955, 9, 21), new Address("Prince Street", "ABC-123", "Mexico City", "Mexico"), new Address("Av. de Segovia", "ZZZ-ZZZ", "Valladolid", "Spain"));
        long accountHolderRepositoryInitialSize = accountHolderRepository.count();
        accountHolderRepository.save(accountHolder3);
        long accountHolderRepositoryFinalSize = accountHolderRepository.count();
        assertEquals(accountHolderRepositoryInitialSize + 1, accountHolderRepositoryFinalSize);
    }

    @Test
    void readAccountHolder_Valid_Read() {
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(accountHolder1.getId());
        assertTrue(accountHolder.isPresent());
        assertEquals("John Adams", accountHolder.get().getName());
        Assertions.assertTrue(EncryptionUtil.matches("12345", accountHolder.get().getPassword()));
        assertEquals(LocalDate.of(1970, 1, 1), accountHolder.get().getBirthDate());
        assertEquals("King Street", accountHolder.get().getPrimaryAddress().getAddress());
        assertEquals("2000-123", accountHolder.get().getPrimaryAddress().getPostalCode());
        assertEquals("London", accountHolder.get().getPrimaryAddress().getCity());
        assertEquals("United Kingdom", accountHolder.get().getPrimaryAddress().getCountry());
    }

    @Test
    void updateAccountHolder_Valid_Updated() {
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(accountHolder2.getId());
        long accountRepositoryInitialSize = accountHolderRepository.count();
        assertTrue(accountHolder.isPresent());
        accountHolder.get().setMailingAddress(new Address("Rua dos Patos", "3123-123", "Leiria", "Portugal"));
        accountHolderRepository.save(accountHolder.get());
        long accountRepositorySizeAfterUpdate = accountHolderRepository.count();
        accountHolder = accountHolderRepository.findById(accountHolder2.getId());
        assertEquals(accountRepositoryInitialSize, accountRepositorySizeAfterUpdate);
        assertEquals("Rua dos Patos", accountHolder.get().getMailingAddress().getAddress());
        assertEquals("3123-123", accountHolder.get().getMailingAddress().getPostalCode());
        assertEquals("Leiria", accountHolder.get().getMailingAddress().getCity());
        assertEquals("Portugal", accountHolder.get().getMailingAddress().getCountry());
    }

    @Test
    void deleteAccountHolder_Valid_Deleted() {
        long accountHolderRepInitialSize = accountHolderRepository.count();
        accountHolderRepository.deleteById(accountHolder1.getId());
        long accountHolderRepSizeAfterDelete = accountHolderRepository.count();
        assertEquals(accountHolderRepInitialSize - 1, accountHolderRepSizeAfterDelete);
        accountHolderRepository.deleteById(accountHolder2.getId());
        long accountHolderRepFinalSize = accountHolderRepository.count();
        assertEquals(accountHolderRepSizeAfterDelete - 1, accountHolderRepFinalSize);
    }
}