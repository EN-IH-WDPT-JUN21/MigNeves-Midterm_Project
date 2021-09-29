package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.controller.dto.AccountDTO;
import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.AccountType;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class AccountControllerTest {

    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    AccountController accountController;
    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void getAccountBalanceById_ValidAdmin_AccountReturned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/account/CC_1").with(httpBasic("Admin", "admin"))).andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("1141.68"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2000-01-01"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ACTIVE"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("40.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
    }

    @Test
    void getAccountBalanceById_ValidAccountHolder_AccountReturned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/account/CC_1").with(httpBasic("Jim Halpert", "Beesly"))).andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("1141.68"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2000-01-01"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ACTIVE"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("40.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
    }

    @Test
    void getAccountBalanceById_InvalidAccountHolder_Exception() throws Exception {
        mockMvc.perform(get("/account/CC_2").with(httpBasic("Jim Halpert", "Beesly"))).andExpect(status().isForbidden()).andReturn();
    }


    @Test
    void updateBalanceById_CreditCard() throws Exception {
        String body = objectMapper.writeValueAsString(new BalanceDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR"))));

        MvcResult mvcResult = mockMvc.perform(patch("/balance/CC_1").with(httpBasic("Admin", "admin"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("500.00"));
    }

    @Test
    void updateBalanceById_Savings() throws Exception {
        String body = objectMapper.writeValueAsString(new BalanceDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR"))));

        MvcResult mvcResult = mockMvc.perform(patch("/balance/SA_3").with(httpBasic("Admin", "admin"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("500.00"));
    }

    @Test
    void updateBalanceById_Checking() throws Exception {
        String body = objectMapper.writeValueAsString(new BalanceDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR"))));

        MvcResult mvcResult = mockMvc.perform(patch("/balance/CH_4").with(httpBasic("Admin", "admin"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("500.00"));
    }

    @Test
    void updateBalanceById_StudentChecking() throws Exception {
        String body = objectMapper.writeValueAsString(new BalanceDTO(new Money(BigDecimal.valueOf(500), Currency.getInstance("EUR"))));

        MvcResult mvcResult = mockMvc.perform(patch("/balance/SC_5").with(httpBasic("Admin", "admin"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("500.00"));
    }

    @Test
    void transferToAccountByOwnerAndId() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("CC_1", "CC_2",
                "Ryan Howard", new Money(BigDecimal.valueOf(1), Currency.getInstance("EUR")));
        String body = objectMapper.writeValueAsString(transactionRequest);
        MvcResult mvcResult = mockMvc.perform(patch("/transfer").with(httpBasic("Jim Halpert", "Beesly"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("1140.68"));
    }

    @Test
    void transferToAccountByOwnerAndId_MinimumBalancePenalty() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("CH_4", "CC_2",
                "Ryan Howard", new Money(BigDecimal.valueOf(600), Currency.getInstance("EUR")));
        String body = objectMapper.writeValueAsString(transactionRequest);
        MvcResult mvcResult = mockMvc.perform(patch("/transfer").with(httpBasic("Pam Beesly", "Jimbo"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("148.00"));

        transactionRequest = new TransactionRequest("SA_3", "CC_2",
                "Ryan Howard", new Money(BigDecimal.valueOf(600), Currency.getInstance("EUR")));
        body = objectMapper.writeValueAsString(transactionRequest);
        mvcResult = mockMvc.perform(patch("/transfer").with(httpBasic("Dwight Schrute", "beets"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("60.00"));
    }

    @Test
    void transferToAccountByOwnerAndId_NegativeAmount_Exception() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("CC_1", "CC_2",
                "Ryan Howard", new Money(BigDecimal.valueOf(-20), Currency.getInstance("EUR")));
        String body = objectMapper.writeValueAsString(transactionRequest);
        MvcResult mvcResult = mockMvc.perform(patch("/transfer").with(httpBasic("Jim Halpert", "Beesly"))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void transferThirdParty() throws Exception {
        ThirdPartyTransactionRequest transactionRequest = new ThirdPartyTransactionRequest(ThirdPartyTransactionType.SEND, "CC_1",
                "$2a$13$RFqms8bfaoMrCjKx9togCulladxRmx6acQn8xWFQ5jKhT.RNqzdKK",
                new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"))
        );
        String body = objectMapper.writeValueAsString(transactionRequest);
        MvcResult mvcResult = mockMvc.perform(patch("/transfer/1842218853")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("12.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SEND"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Michael Scott"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SEND"));
    }

    @Test
    void getAllAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/accounts").with(httpBasic("Jim Halpert", "Beesly")))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("CC_1"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CH_4"));
    }

    @Test
    void createCreditCard() throws Exception {
        AccountDTO creditCardDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.CREDIT_CARD, new Money(BigDecimal.valueOf(50), Currency.getInstance("EUR")), BigDecimal.valueOf(0.05));
        String body = objectMapper.writeValueAsString(creditCardDTO);
        MvcResult mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.1"));

        creditCardDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.CREDIT_CARD, new Money(BigDecimal.valueOf(9000000), Currency.getInstance("EUR")), BigDecimal.valueOf(1));
        body = objectMapper.writeValueAsString(creditCardDTO);
        mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100000"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2"));

        creditCardDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.CREDIT_CARD, null, null);
        body = objectMapper.writeValueAsString(creditCardDTO);
        mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.1"));
    }

    @Test
    void createChecking() throws Exception {
        AccountDTO accountDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")),
                1L, 2L, AccountType.CHECKING);
        String body = objectMapper.writeValueAsString(accountDTO);
        MvcResult mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("12.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("250.00"));

        accountDTO = new AccountDTO(new Money(BigDecimal.valueOf(123.99), Currency.getInstance("EUR")),
                5L, 3L, AccountType.CHECKING);
        body = objectMapper.writeValueAsString(accountDTO);
        mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("123.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pete Miller"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Ryan Howard"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("12.00"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("250.00"));
    }

    @Test
    void createSavings() throws Exception {
        AccountDTO savingsDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.SAVINGS, new Money(BigDecimal.valueOf(50), Currency.getInstance("EUR")), BigDecimal.valueOf(0));
        String body = objectMapper.writeValueAsString(savingsDTO);
        MvcResult mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100.00"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0"));

        savingsDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.SAVINGS, new Money(BigDecimal.valueOf(3000), Currency.getInstance("EUR")), BigDecimal.valueOf(1));
        body = objectMapper.writeValueAsString(savingsDTO);
        mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.5"));

        savingsDTO = new AccountDTO(new Money(BigDecimal.valueOf(999.99), Currency.getInstance("EUR")), 1L, 2L,
                AccountType.SAVINGS, null, null);
        body = objectMapper.writeValueAsString(savingsDTO);
        mvcResult = mockMvc.perform(post("/create/account").with(httpBasic("Admin", "admin"))
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("999.99"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jim Halpert"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pam Beesly"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025"));
    }
}