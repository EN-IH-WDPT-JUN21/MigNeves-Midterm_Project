package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.ThirdParty;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
class ThirdPartyRepositoryTest {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    ThirdParty thirdParty1;
    ThirdParty thirdParty2;
    int hashedKey1;
    int hashedKey2;

    @BeforeEach
    void setUp() {
        thirdParty1 = new ThirdParty("Ze Bino");
        hashedKey1 = thirdParty1.getHashedKey();
        thirdParty2 = new ThirdParty("Asdrubal");
        hashedKey2 = thirdParty2.getHashedKey();
        thirdPartyRepository.saveAll(List.of(thirdParty1, thirdParty2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createThirdParty_Valid_Created() {
        ThirdParty thirdParty3 = new ThirdParty("Herman");
        long thirdPartyRepositoryInitialSize = thirdPartyRepository.count();
        thirdPartyRepository.save(thirdParty3);
        long thirdPartyRepositoryFinalSize = thirdPartyRepository.count();
        assertEquals(thirdPartyRepositoryInitialSize + 1, thirdPartyRepositoryFinalSize);
    }

    @Test
    void readThirdParty_Valid_Read() {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(thirdParty1.getId());
        assertTrue(thirdParty.isPresent());
        assertEquals("Ze Bino", thirdParty.get().getName());
        assertEquals(hashedKey1, thirdParty.get().getHashedKey());
    }

    @Test
    void updateThirdParty_Valid_Updated() {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(thirdParty2.getId());
        long thirdPartyRepositoryInitialSize = thirdPartyRepository.count();
        assertTrue(thirdParty.isPresent());
        thirdParty.get().setName("Pedro");
        thirdPartyRepository.save(thirdParty.get());
        long thirdPartyRepositorySizeAfterUpdate = thirdPartyRepository.count();
        thirdParty = thirdPartyRepository.findById(thirdParty2.getId());
        assertEquals("Pedro", thirdParty.get().getName());
        assertEquals(hashedKey2, thirdParty.get().getHashedKey());
        assertNotEquals(hashedKey2, thirdParty.get().hashCode());
        assertEquals(thirdPartyRepositoryInitialSize, thirdPartyRepositorySizeAfterUpdate);
    }

    @Test
    void deleteThirdParty_Valid_Deleted() {
        long thirdPartyRepInitialSize = thirdPartyRepository.count();
        thirdPartyRepository.deleteById(thirdParty1.getId());
        long thirdPartyRepSizeAfterDelete = thirdPartyRepository.count();
        assertEquals(thirdPartyRepInitialSize - 1, thirdPartyRepSizeAfterDelete);
        thirdPartyRepository.deleteById(thirdParty2.getId());
        long thirdPartyRepFinalSize = thirdPartyRepository.count();
        assertEquals(thirdPartyRepSizeAfterDelete - 1, thirdPartyRepFinalSize);
    }

    @Test
    void findByHashedKey() {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey1);
        assertEquals("Ze Bino", thirdParty.get().getName());
        assertEquals(hashedKey1, thirdParty.get().getHashedKey());
    }
}