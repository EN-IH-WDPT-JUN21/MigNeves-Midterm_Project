package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.enums.AccountType;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

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
        "server.error.include-message = always",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
})
class ValidatorTest {

    @Autowired
    Validator validator;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateTransaction() {
        TransactionRequest transactionRequest = new TransactionRequest("CC_1", "CC_2", "Ryan Howard",
                new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")));
        List<Account> accountList = validator.validateTransaction("Jim Halpert", transactionRequest);
        assertEquals("CC_1", accountList.get(0).getId());
        assertEquals("CC_2", accountList.get(1).getId());

        transactionRequest.setToAccountId("CC_1");
        Throwable exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction("Jim Halpert", transactionRequest));
        assertEquals("400 BAD_REQUEST \"The sending and receiving accounts must not be the same!\"", exception.getMessage());

        transactionRequest.setFromAccountId("SA_3");
        transactionRequest.setToAccountId("CC_2");
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction("Jim Halpert", transactionRequest));
        assertEquals("404 NOT_FOUND \"404 NOT_FOUND \"Account with id SA_3 does not have a primary owner named Jim Halpert\"\"", exception.getMessage());

        transactionRequest.setFromAccountId("CC_1");
        transactionRequest.setToAccountId("SA_3");
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction("Jim Halpert", transactionRequest));
        assertEquals("404 NOT_FOUND \"404 NOT_FOUND \"Account with id SA_3 does not have a owner named Ryan Howard\"\"", exception.getMessage());

        transactionRequest.setToAccountId("CC_2");
        transactionRequest.setTransfer(new Money(BigDecimal.valueOf(1000000), Currency.getInstance("EUR")));
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction("Jim Halpert", transactionRequest));
        assertEquals("403 FORBIDDEN \"403 FORBIDDEN \"Account with id CC_1 doesn't have the necessary funds to transfer\"\"", exception.getMessage());

        transactionRequest.setTransfer(new Money(BigDecimal.valueOf(-200), Currency.getInstance("EUR")));
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction("Jim Halpert", transactionRequest));
        assertEquals("400 BAD_REQUEST \"400 BAD_REQUEST \"The transaction amount must be positive\"\"", exception.getMessage());
    }

    @Test
    void validateThirdPartyTransaction() {
        ThirdPartyTransactionRequest thirdPartyTransactionRequest = new ThirdPartyTransactionRequest(ThirdPartyTransactionType.SEND, "CC_1", "$2a$13$RFqms8bfaoMrCjKx9togCulladxRmx6acQn8xWFQ5jKhT.RNqzdKK",
                new Money(BigDecimal.valueOf(1000000), Currency.getInstance("EUR")));
        ThirdPartyTransaction thirdPartyTransaction = validator.validateTransaction(1842218853, thirdPartyTransactionRequest);
        assertEquals(BigDecimal.valueOf(1000000).setScale(2, RoundingMode.HALF_EVEN), thirdPartyTransaction.getTransfer().getAmount());

        Throwable exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction(111111111, thirdPartyTransactionRequest));
        assertEquals("404 NOT_FOUND \"404 NOT_FOUND \"There is no ThirdParty Account with a hashed key 111111111\"\"", exception.getMessage());

        thirdPartyTransactionRequest.setToAccountId("SA_3");
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction(1842218853, thirdPartyTransactionRequest));
        assertEquals("403 FORBIDDEN \"403 FORBIDDEN \"The secretKey provided doesn't match! Unable to transfer\"\"", exception.getMessage());

        thirdPartyTransactionRequest.setToAccountId("CC_1");
        thirdPartyTransactionRequest.setTransactionType(ThirdPartyTransactionType.RECEIVE);
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction(1842218853, thirdPartyTransactionRequest));
        assertEquals("403 FORBIDDEN \"403 FORBIDDEN \"Account with id CC_1 doesn't have the necessary funds to transfer\"\"", exception.getMessage());

        thirdPartyTransactionRequest.setTransfer(new Money(BigDecimal.valueOf(-200), Currency.getInstance("EUR")));
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateTransaction(1842218853, thirdPartyTransactionRequest));
        assertEquals("400 BAD_REQUEST \"400 BAD_REQUEST \"The transaction amount must be positive\"\"", exception.getMessage());
    }

    @Test
    void validateOwners() {
        AccountDTO creditCardDTO = new AccountDTO(new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR")),
                1L, 2L, AccountType.CREDIT_CARD);
        List<AccountHolder> accountHolderList = validator.validateOwners(creditCardDTO);
        assertEquals(creditCardDTO.getPrimaryOwnerId(), accountHolderList.get(0).getId());
        assertEquals(creditCardDTO.getSecondaryOwnerId(), accountHolderList.get(1).getId());

        creditCardDTO.setPrimaryOwnerId(10L);
        Throwable exception = assertThrows(ResponseStatusException.class, () -> validator.validateOwners(creditCardDTO));
        assertEquals("404 NOT_FOUND \"Unable to find AccountHolder with id 10\"", exception.getMessage());

        creditCardDTO.setPrimaryOwnerId(1L);
        creditCardDTO.setSecondaryOwnerId(11L);
        exception = assertThrows(ResponseStatusException.class, () -> validator.validateOwners(creditCardDTO));
        assertEquals("404 NOT_FOUND \"Unable to find AccountHolder with id 11\"", exception.getMessage());
    }
}