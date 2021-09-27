package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.StudentChecking;
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
class StudentCheckingRepositoryTest {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    StudentChecking studentChecking1;
    StudentChecking studentChecking2;
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("Kelly", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        accountHolder2 = new AccountHolder("Angela", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        studentChecking1 = new StudentChecking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder1);
        studentChecking2 = new StudentChecking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder1, accountHolder2);
        studentCheckingRepository.saveAll(List.of(studentChecking1, studentChecking2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createStudentChecking_Valid_Created() {
        StudentChecking studentChecking3 = new StudentChecking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), accountHolder2);
        long studentCheckingRepositoryInitialSize = studentCheckingRepository.count();
        studentCheckingRepository.save(studentChecking3);
        long studentCheckingRepositoryFinalSize = studentCheckingRepository.count();
        assertEquals(studentCheckingRepositoryInitialSize + 1, studentCheckingRepositoryFinalSize);
    }

    @Test
    void readStudentChecking_Valid_Read() {
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(studentChecking2.getId());
        assertTrue(studentChecking.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")).getAmount(), studentChecking.get().getBalance().getAmount());
        assertEquals(Status.ACTIVE, studentChecking.get().getStatus());
        assertEquals("Kelly", studentChecking.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", studentChecking.get().getPrimaryOwner().getPassword()));
        assertEquals(30, studentChecking.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Angela", studentChecking.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", studentChecking.get().getSecondaryOwner().getPassword()));
        assertEquals(90, studentChecking.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateStudentChecking_Valid_Updated() {
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(studentChecking1.getId());
        long studentCheckingRepositoryInitialSize = studentCheckingRepository.count();
        assertTrue(studentChecking.isPresent());
        studentChecking.get().setBalance(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")));
        studentChecking.get().setPrimaryOwner(accountHolder2);
        studentCheckingRepository.save(studentChecking.get());
        long studentCheckingRepositorySizeAfterUpdate = studentCheckingRepository.count();
        studentChecking = studentCheckingRepository.findById(studentChecking1.getId());
        assertEquals(studentCheckingRepositoryInitialSize, studentCheckingRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")).getAmount(), studentChecking.get().getBalance().getAmount());
        assertEquals(90, studentChecking.get().getPrimaryOwner().getAge());
        assertEquals("Av. Duarte", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteStudentChecking_Valid_Deleted() {
        long studentCheckingRepInitialSize = studentCheckingRepository.count();
        studentCheckingRepository.deleteById(studentChecking1.getId());
        long studentCheckingRepSizeAfterDelete = studentCheckingRepository.count();
        assertEquals(studentCheckingRepInitialSize - 1, studentCheckingRepSizeAfterDelete);
        studentCheckingRepository.deleteById(studentChecking2.getId());
        long studentCheckingRepFinalSize = studentCheckingRepository.count();
        assertEquals(studentCheckingRepSizeAfterDelete - 1, studentCheckingRepFinalSize);
    }

    @Test
    void findByPrimaryOwner() {
        int studentCheckingOwnedByKelly = studentCheckingRepository.findByPrimaryOwner(accountHolder1.getId()).size();
        assertEquals(2, studentCheckingOwnedByKelly);
    }

    @Test
    void findBySecondaryOwner() {
        int studentCheckingCoOwnedByAngela = studentCheckingRepository.findBySecondaryOwner(accountHolder2.getId()).size();
        assertEquals(1, studentCheckingCoOwnedByAngela);
    }
}