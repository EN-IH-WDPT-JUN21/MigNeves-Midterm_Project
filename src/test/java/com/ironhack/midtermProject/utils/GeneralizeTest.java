package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.ListOfAccounts;
import com.ironhack.midtermProject.dao.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        "server.error.include-message = always",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
})
class GeneralizeTest {

    @Autowired
    Generalize generalize;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAccountFromId() {
        Account account = generalize.getAccountFromId("CC_1");
        assertEquals(CreditCard.class, account.getClass());
        account = generalize.getAccountFromId("SA_3");
        assertEquals(Savings.class, account.getClass());
        account = generalize.getAccountFromId("CH_4");
        assertEquals(Checking.class, account.getClass());
        account = generalize.getAccountFromId("SC_5");
        assertEquals(StudentChecking.class, account.getClass());
        assertThrows(IllegalArgumentException.class, () -> generalize.getAccountFromId("AA_123"));
        assertThrows(ResponseStatusException.class, () -> generalize.getAccountFromId("CC_5"));
    }

    @Test
    void getAllAccounts() {
        ListOfAccounts listOfAccounts = generalize.getAllAccounts(1L);
        assertEquals(1, listOfAccounts.getPrimaryCreditCardAccounts().size());
        assertEquals(0, listOfAccounts.getPrimarySavingsAccounts().size());
        assertEquals(0, listOfAccounts.getPrimaryCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getPrimaryStudentCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getSecondaryCreditCardAccounts().size());
        assertEquals(0, listOfAccounts.getSecondarySavingsAccounts().size());
        assertEquals(1, listOfAccounts.getSecondaryCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getSecondaryStudentCheckingAccounts().size());
        listOfAccounts = generalize.getAllAccounts(4L);
        assertEquals(0, listOfAccounts.getPrimaryCreditCardAccounts().size());
        assertEquals(1, listOfAccounts.getPrimarySavingsAccounts().size());
        assertEquals(0, listOfAccounts.getPrimaryCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getPrimaryStudentCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getSecondaryCreditCardAccounts().size());
        assertEquals(0, listOfAccounts.getSecondarySavingsAccounts().size());
        assertEquals(0, listOfAccounts.getSecondaryCheckingAccounts().size());
        assertEquals(0, listOfAccounts.getSecondaryStudentCheckingAccounts().size());
    }
}