package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.AddressDTO;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class AccountHolderControllerTest {

    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderController accountHolderController;
    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAccountHolder() throws Exception {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Senator", "Oscar", LocalDate.of(1960, 10, 1), new AddressDTO("street", "city", "country", "1111-111"), null);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult mvcResult = mockMvc.perform(post("/create/user")
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("6"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Senator"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("street"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("city"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("country"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1111-111"));

        accountHolderDTO = new AccountHolderDTO("Senator Jr.", "Oscar", LocalDate.of(1960, 10, 1), new AddressDTO("street", "city", "country", "1111-111"), new AddressDTO("street2", "city2", "country2", "2222-222"));
        body = objectMapper.writeValueAsString(accountHolderDTO);
        mvcResult = mockMvc.perform(post("/create/user")
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("6"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Senator"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("street"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("city"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("country"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1111-111"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Senator Jr."));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("street2"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("city2"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("country2"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2222-222"));
    }

    @Test
    void createAccountHolder_DuplicateName_Error() throws Exception {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Jim Halpert", "Oscar", LocalDate.of(1960, 10, 1), new AddressDTO("street", "city", "country", "1111-111"), null);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult mvcResult = mockMvc.perform(post("/create/user")
                .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();
    }
}