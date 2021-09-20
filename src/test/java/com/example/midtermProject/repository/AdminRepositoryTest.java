package com.example.midtermProject.repository;

import com.example.midtermProject.dao.Admin;
import com.example.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    Admin admin1;
    Admin admin2;

    @BeforeEach
    void setUp() {
        admin1 = new Admin("Luis","@dm1n");
        admin2 = new Admin("Jack", "baiter");
        adminRepository.saveAll(List.of(admin1, admin2));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void createAdmin_Valid_Created(){
        Admin admin3 = new Admin("Rachel", "love_animals");
        int adminRepositoryInitialSize = adminRepository.findAll().size();
        adminRepository.save(admin3);
        int adminRepositoryFinalSize = adminRepository.findAll().size();
        assertEquals(adminRepositoryInitialSize + 1, adminRepositoryFinalSize);
    }

    @Test
    void readAdmin_Valid_Read(){
        Optional<Admin> admin = adminRepository.findById(admin1.getId());
        assertTrue(admin.isPresent());
        assertEquals("Luis", admin.get().getName());
        assertTrue(EncryptionUtil.matches("@dm1n", admin.get().getPassword()));
    }

    @Test
    void updateAdmin_Valid_Updated(){
        Optional<Admin> admin = adminRepository.findById(admin2.getId());
        int adminRepositoryInitialSize = adminRepository.findAll().size();
        assertTrue(admin.isPresent());
        admin.get().setPassword("AbCdE");
        adminRepository.save(admin.get());
        int adminRepositorySizeAfterUpdate = adminRepository.findAll().size();
        admin = adminRepository.findById(admin2.getId());
        assertEquals(adminRepositoryInitialSize, adminRepositorySizeAfterUpdate);
        assertTrue(EncryptionUtil.matches("AbCdE", admin.get().getPassword()));
    }

    @Test
    void deleteAdmin_Valid_Deleted(){
        int adminRepInitialSize = adminRepository.findAll().size();
        adminRepository.deleteById(admin1.getId());
        int adminRepSizeAfterDelete = adminRepository.findAll().size();
        assertEquals(adminRepInitialSize - 1, adminRepSizeAfterDelete);
        adminRepository.deleteById(admin2.getId());
        int adminRepFinalSize = adminRepository.findAll().size();
        assertEquals(adminRepSizeAfterDelete - 1, adminRepFinalSize);
    }
}