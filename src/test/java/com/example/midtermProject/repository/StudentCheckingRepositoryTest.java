package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Address;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.enums.Status;
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
class StudentCheckingRepositoryTest {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    StudentChecking studentChecking1;
    StudentChecking studentChecking2;
    AccountHolder accountHolder;
    AccountHolder secondaryHolder;

    @BeforeEach
    void setUp() {
        accountHolder = new AccountHolder("João Neves", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        secondaryHolder = new AccountHolder("Unkown", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder, secondaryHolder));
        studentChecking1 = new StudentChecking(new Money(BigDecimal.valueOf(1100), Currency.getInstance("EUR")), accountHolder);
        studentChecking2 = new StudentChecking(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")), accountHolder, secondaryHolder);
        studentCheckingRepository.saveAll(List.of(studentChecking1, studentChecking2));
    }

    @AfterEach
    void tearDown() {
        studentCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void createStudentChecking_Valid_Created(){
        StudentChecking studentChecking3 = new StudentChecking(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR")), secondaryHolder);
        int studentCheckingRepositoryInitialSize = studentCheckingRepository.findAll().size();
        studentCheckingRepository.save(studentChecking3);
        int studentCheckingRepositoryFinalSize = studentCheckingRepository.findAll().size();
        assertEquals(studentCheckingRepositoryInitialSize + 1, studentCheckingRepositoryFinalSize);
    }

    @Test
    void readStudentChecking_Valid_Read(){
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(studentChecking2.getId());
        assertTrue(studentChecking.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(2100), Currency.getInstance("EUR")).getAmount(), studentChecking.get().getBalance().getAmount());
        assertEquals(Status.ACTIVE, studentChecking.get().getStatus());
        assertEquals("João Neves", studentChecking.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", studentChecking.get().getPrimaryOwner().getPassword()));
        assertEquals(30, studentChecking.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", studentChecking.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Unkown", studentChecking.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", studentChecking.get().getSecondaryOwner().getPassword()));
        assertEquals(90, studentChecking.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", studentChecking.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateStudentChecking_Valid_Updated(){
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(studentChecking1.getId());
        int studentCheckingRepositoryInitialSize = studentCheckingRepository.findAll().size();
        assertTrue(studentChecking.isPresent());
        studentChecking.get().setBalance(new Money(BigDecimal.valueOf(30000), Currency.getInstance("EUR")));
        studentChecking.get().setPrimaryOwner(secondaryHolder);
        studentCheckingRepository.save(studentChecking.get());
        int studentCheckingRepositorySizeAfterUpdate = studentCheckingRepository.findAll().size();
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
    void deleteStudentChecking_Valid_Deleted(){
        int studentCheckingRepInitialSize = studentCheckingRepository.findAll().size();
        studentCheckingRepository.deleteById(studentChecking1.getId());
        int studentCheckingRepSizeAfterDelete = studentCheckingRepository.findAll().size();
        assertEquals(studentCheckingRepInitialSize - 1, studentCheckingRepSizeAfterDelete);
        studentCheckingRepository.deleteById(studentChecking2.getId());
        int studentCheckingRepFinalSize = studentCheckingRepository.findAll().size();
        assertEquals(studentCheckingRepSizeAfterDelete - 1, studentCheckingRepFinalSize);
    }
}