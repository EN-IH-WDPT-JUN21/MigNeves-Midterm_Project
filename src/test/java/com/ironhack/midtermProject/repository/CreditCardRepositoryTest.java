package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
class CreditCardRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    CreditCard creditCard1;
    CreditCard creditCard2;
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;

    @BeforeEach
    void setUp() {
        accountHolder1 = new AccountHolder("Kelly", "winter1sComing", LocalDate.of(1988, 5, 10), new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        accountHolder2 = new AccountHolder("Angela", "whoAmI", LocalDate.of(1921, 12, 21), new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        creditCard1 = new CreditCard(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),
                accountHolder1,
                new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.3));
        creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(175), Currency.getInstance("EUR")),
                accountHolder1,
                accountHolder2,
                new Money(BigDecimal.valueOf(90), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.5));
        creditCardRepository.saveAll(List.of(creditCard1, creditCard2));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCreditCard_Valid_Created() {
        CreditCard creditCard3 = new CreditCard(new Money(BigDecimal.valueOf(400), Currency.getInstance("EUR")),
                accountHolder1,
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")),
                BigDecimal.valueOf(0.4));
        long creditCardRepositoryInitialSize = creditCardRepository.count();
        creditCardRepository.save(creditCard3);
        long creditCardRepositoryFinalSize = creditCardRepository.count();
        assertEquals(creditCardRepositoryInitialSize + 1, creditCardRepositoryFinalSize);
    }

    @Test
    void readCreditCard_Valid_Read() {
        Optional<CreditCard> creditCard = creditCardRepository.findById(creditCard2.getId());
        assertTrue(creditCard.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(175), Currency.getInstance("EUR")).getAmount(), creditCard.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")).getAmount(), creditCard.get().getCreditLimit().getAmount());
        assertEquals(BigDecimal.valueOf(0.2).setScale(4, RoundingMode.HALF_EVEN), creditCard.get().getInterestRate());
        assertEquals("Kelly", creditCard.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", creditCard.get().getPrimaryOwner().getPassword()));
        assertEquals(LocalDate.of(1988, 5, 10), creditCard.get().getPrimaryOwner().getBirthDate());
        assertEquals("Rua das Cerejeiras", creditCard.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", creditCard.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Angela", creditCard.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", creditCard.get().getSecondaryOwner().getPassword()));
        assertEquals(LocalDate.of(1921, 12, 21), creditCard.get().getSecondaryOwner().getBirthDate());
        assertEquals("Av. Duarte", creditCard.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", creditCard.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", creditCard.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateCreditCard_Valid_Updated() {
        Optional<CreditCard> creditCard = creditCardRepository.findById(creditCard1.getId());
        long creditCardRepositoryInitialSize = creditCardRepository.count();
        assertTrue(creditCard.isPresent());
        creditCard.get().setCreditLimit(new Money(BigDecimal.valueOf(300000), Currency.getInstance("EUR")));
        creditCard.get().setInterestRate(BigDecimal.valueOf(0.05));
        creditCardRepository.save(creditCard.get());
        long creditCardRepositorySizeAfterUpdate = creditCardRepository.count();
        creditCard = creditCardRepository.findById(creditCard1.getId());
        assertEquals(creditCardRepositoryInitialSize, creditCardRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")).getAmount(), creditCard.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(100000), Currency.getInstance("EUR")).getAmount(), creditCard.get().getCreditLimit().getAmount());
        assertEquals(BigDecimal.valueOf(0.1).setScale(4, RoundingMode.HALF_EVEN), creditCard.get().getInterestRate());
        assertEquals("Kelly", creditCard.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", creditCard.get().getPrimaryOwner().getPassword()));
        assertEquals(LocalDate.of(1988, 5, 10), creditCard.get().getPrimaryOwner().getBirthDate());
        assertEquals("Rua das Cerejeiras", creditCard.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", creditCard.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteCreditCard_Valid_Deleted() {
        long creditCardRepInitialSize = creditCardRepository.count();
        creditCardRepository.deleteById(creditCard1.getId());
        long creditCardRepSizeAfterDelete = creditCardRepository.count();
        assertEquals(creditCardRepInitialSize - 1, creditCardRepSizeAfterDelete);
        creditCardRepository.deleteById(creditCard2.getId());
        long creditCardRepFinalSize = creditCardRepository.count();
        assertEquals(creditCardRepSizeAfterDelete - 1, creditCardRepFinalSize);
    }

    @Test
    void findByPrimaryOwner() {
        int creditCardOwnedByKelly = creditCardRepository.findByPrimaryOwner(accountHolder1.getId()).size();
        assertEquals(2, creditCardOwnedByKelly);
    }

    @Test
    void findBySecondaryOwner() {
        int creditCardCoOwnedByAngela = creditCardRepository.findBySecondaryOwner(accountHolder2.getId()).size();
        assertEquals(1, creditCardCoOwnedByAngela);
    }
}