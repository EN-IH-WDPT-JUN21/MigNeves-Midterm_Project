package com.example.midtermProject.repository;

import com.example.midtermProject.dao.ThirdParty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        thirdPartyRepository.deleteAll();
    }

    @Test
    void createThirdParty_Valid_Created(){
        ThirdParty thirdParty3 = new ThirdParty("Herman");
        int thirdPartyRepositoryInitialSize = thirdPartyRepository.findAll().size();
        thirdPartyRepository.save(thirdParty3);
        int thirdPartyRepositoryFinalSize = thirdPartyRepository.findAll().size();
        assertEquals(thirdPartyRepositoryInitialSize + 1, thirdPartyRepositoryFinalSize);
    }

    @Test
    void readThirdParty_Valid_Read(){
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(thirdParty1.getId());
        assertTrue(thirdParty.isPresent());
        assertEquals("Ze Bino", thirdParty.get().getName());
        assertEquals(hashedKey1, thirdParty.get().getHashedKey());
    }

    @Test
    void updateThirdParty_Valid_Updated(){
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findById(thirdParty2.getId());
        int thirdPartyRepositoryInitialSize = thirdPartyRepository.findAll().size();
        assertTrue(thirdParty.isPresent());
        thirdParty.get().setName("Pedro");
        thirdPartyRepository.save(thirdParty.get());
        int thirdPartyRepositorySizeAfterUpdate = thirdPartyRepository.findAll().size();
        thirdParty = thirdPartyRepository.findById(thirdParty2.getId());
        assertEquals("Pedro", thirdParty.get().getName());
        assertEquals(hashedKey2, thirdParty.get().getHashedKey());
        assertNotEquals(hashedKey2, thirdParty.get().hashCode());
        assertEquals(thirdPartyRepositoryInitialSize, thirdPartyRepositorySizeAfterUpdate);
    }

    @Test
    void deleteThirdParty_Valid_Deleted(){
        int thirdPartyRepInitialSize = thirdPartyRepository.findAll().size();
        thirdPartyRepository.deleteById(thirdParty1.getId());
        int thirdPartyRepSizeAfterDelete = thirdPartyRepository.findAll().size();
        assertEquals(thirdPartyRepInitialSize - 1, thirdPartyRepSizeAfterDelete);
        thirdPartyRepository.deleteById(thirdParty2.getId());
        int thirdPartyRepFinalSize = thirdPartyRepository.findAll().size();
        assertEquals(thirdPartyRepSizeAfterDelete - 1, thirdPartyRepFinalSize);
    }
}