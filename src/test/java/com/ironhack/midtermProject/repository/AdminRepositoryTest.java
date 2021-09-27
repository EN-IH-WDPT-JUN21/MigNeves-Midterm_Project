package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.Admin;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

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
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    Admin admin1;
    Admin admin2;

    @BeforeEach
    void setUp() {
        admin1 = new Admin("Luis", "@dm1n");
        admin2 = new Admin("Jack", "baiter");
        adminRepository.saveAll(List.of(admin1, admin2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAdmin_Valid_Created() {
        Admin admin3 = new Admin("Rachel", "love_animals");
        long adminRepositoryInitialSize = adminRepository.count();
        adminRepository.save(admin3);
        long adminRepositoryFinalSize = adminRepository.count();
        assertEquals(adminRepositoryInitialSize + 1, adminRepositoryFinalSize);
    }

    @Test
    void readAdmin_Valid_Read() {
        Optional<Admin> admin = adminRepository.findById(admin1.getId());
        assertTrue(admin.isPresent());
        assertEquals("Luis", admin.get().getName());
        Assertions.assertTrue(EncryptionUtil.matches("@dm1n", admin.get().getPassword()));
    }

    @Test
    void updateAdmin_Valid_Updated() {
        Optional<Admin> admin = adminRepository.findById(admin2.getId());
        long adminRepositoryInitialSize = adminRepository.count();
        assertTrue(admin.isPresent());
        admin.get().setPassword("AbCdE");
        adminRepository.save(admin.get());
        long adminRepositorySizeAfterUpdate = adminRepository.count();
        admin = adminRepository.findById(admin2.getId());
        assertEquals(adminRepositoryInitialSize, adminRepositorySizeAfterUpdate);
        assertTrue(EncryptionUtil.matches("AbCdE", admin.get().getPassword()));
    }

    @Test
    void deleteAdmin_Valid_Deleted() {
        long adminRepInitialSize = adminRepository.count();
        adminRepository.deleteById(admin1.getId());
        long adminRepSizeAfterDelete = adminRepository.count();
        assertEquals(adminRepInitialSize - 1, adminRepSizeAfterDelete);
        adminRepository.deleteById(admin2.getId());
        long adminRepFinalSize = adminRepository.count();
        assertEquals(adminRepSizeAfterDelete - 1, adminRepFinalSize);
    }

    @Test
    void findByName() {
        Optional<Admin> admin = adminRepository.findByName(admin1.getName());
        assertTrue(admin.isPresent());
        assertEquals("Luis", admin.get().getName());
        Assertions.assertTrue(EncryptionUtil.matches("@dm1n", admin.get().getPassword()));
    }
}