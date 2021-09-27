package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.utils.EncryptionUtil;
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
class CheckingRepositoryTest {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    Checking checking1;
    Checking checking2;
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("Kelly", "Ryan", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        accountHolder2 = new AccountHolder("Angela", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        checking1 = new Checking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder1);
        checking2 = new Checking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder1, accountHolder2);
        checkingRepository.saveAll(List.of(checking1, checking2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createChecking_Valid_Created() {
        Checking checking3 = new Checking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), accountHolder2);
        long checkingRepositoryInitialSize = checkingRepository.count();
        checkingRepository.save(checking3);
        long checkingRepositoryFinalSize = checkingRepository.count();
        assertEquals(checkingRepositoryInitialSize + 1, checkingRepositoryFinalSize);
    }

    @Test
    void readChecking_Valid_Read() {
        Optional<Checking> checking = checkingRepository.findById(checking2.getId());
        assertTrue(checking.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")).getAmount(), checking.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(250), Currency.getInstance("EUR")).getAmount(), checking.get().getMinimumBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR")).getAmount(), checking.get().getMonthlyMaintenanceFee().getAmount());
        assertEquals(Status.ACTIVE, checking.get().getStatus());
        assertEquals("Kelly", checking.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("Ryan", checking.get().getPrimaryOwner().getPassword()));
        assertEquals(30, checking.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", checking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", checking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", checking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", checking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Angela", checking.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", checking.get().getSecondaryOwner().getPassword()));
        assertEquals(90, checking.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", checking.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", checking.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", checking.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", checking.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateChecking_Valid_Updated() {
        Optional<Checking> checking = checkingRepository.findById(checking1.getId());
        long checkingRepositoryInitialSize = checkingRepository.count();
        assertTrue(checking.isPresent());
        checking.get().setBalance(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")));
        checking.get().setPrimaryOwner(accountHolder2);
        checkingRepository.save(checking.get());
        long checkingRepositorySizeAfterUpdate = checkingRepository.count();
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
    void deleteChecking_Valid_Deleted() {
        long checkingRepInitialSize = checkingRepository.count();
        checkingRepository.deleteById(checking1.getId());
        long checkingRepSizeAfterDelete = checkingRepository.count();
        assertEquals(checkingRepInitialSize - 1, checkingRepSizeAfterDelete);
        checkingRepository.deleteById(checking2.getId());
        long checkingRepFinalSize = checkingRepository.count();
        assertEquals(checkingRepSizeAfterDelete - 1, checkingRepFinalSize);
    }

    @Test
    void findByPrimaryOwner() {
        int checkingOwnedByKelly = checkingRepository.findByPrimaryOwner(accountHolder1.getId()).size();
        assertEquals(2, checkingOwnedByKelly);
    }

    @Test
    void findBySecondaryOwner() {
        int checkingCoOwnedByAngela = checkingRepository.findBySecondaryOwner(accountHolder2.getId()).size();
        assertEquals(1, checkingCoOwnedByAngela);
    }
}