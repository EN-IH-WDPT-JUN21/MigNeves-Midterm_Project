package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Address;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.dao.Money;
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
class CreditCardRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    CreditCard creditCard1;
    CreditCard creditCard2;
    AccountHolder accountHolder;
    AccountHolder secondaryHolder;

    @BeforeEach
    void setUp() {
        accountHolder = new AccountHolder("João Neves", "winter1sComing", 30, new Address("Rua das Cerejeiras", "1234-432", "Viseu", "Portugal"));
        secondaryHolder = new AccountHolder("Unkown", "whoAmI", 90, new Address("Av. Duarte", "ABC", "Vouzela", "Portugal"));
        accountHolderRepository.saveAll(List.of(accountHolder, secondaryHolder));
        creditCard1 = new CreditCard(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")),
                accountHolder,
                new Money(BigDecimal.valueOf(300), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(0.3), Currency.getInstance("EUR")));
        creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(175), Currency.getInstance("EUR")),
                accountHolder,
                secondaryHolder,
                new Money(BigDecimal.valueOf(90), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(0.5), Currency.getInstance("EUR")));
        creditCardRepository.saveAll(List.of(creditCard1, creditCard2));
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void createCreditCard_Valid_Created(){
        CreditCard creditCard3 = new CreditCard(new Money(BigDecimal.valueOf(400), Currency.getInstance("EUR")),
                accountHolder,
                new Money(BigDecimal.valueOf(200), Currency.getInstance("EUR")),
                new Money(BigDecimal.valueOf(0.4), Currency.getInstance("EUR")));
        int creditCardRepositoryInitialSize = creditCardRepository.findAll().size();
        creditCardRepository.save(creditCard3);
        int creditCardRepositoryFinalSize = creditCardRepository.findAll().size();
        assertEquals(creditCardRepositoryInitialSize + 1, creditCardRepositoryFinalSize);
    }

    @Test
    void readCreditCard_Valid_Read(){
        Optional<CreditCard> creditCard = creditCardRepository.findById(creditCard2.getId());
        assertTrue(creditCard.isPresent());
        assertEquals(new Money(BigDecimal.valueOf(175), Currency.getInstance("EUR")).getAmount(), creditCard.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(90), Currency.getInstance("EUR")).getAmount(), creditCard.get().getCreditLimit().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(0.5), Currency.getInstance("EUR")).getAmount(), creditCard.get().getInterestRate().getAmount());
        assertEquals("João Neves", creditCard.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", creditCard.get().getPrimaryOwner().getPassword()));
        assertEquals(30, creditCard.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", creditCard.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", creditCard.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCountry());
        assertEquals("Unkown", creditCard.get().getSecondaryOwner().getName());
        assertTrue(EncryptionUtil.matches("whoAmI", creditCard.get().getSecondaryOwner().getPassword()));
        assertEquals(90, creditCard.get().getSecondaryOwner().getAge());
        assertEquals("Av. Duarte", creditCard.get().getSecondaryOwner().getPrimaryAddress().getAddress());
        assertEquals("ABC", creditCard.get().getSecondaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Vouzela", creditCard.get().getSecondaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getSecondaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void updateCreditCard_Valid_Updated(){
        Optional<CreditCard> creditCard = creditCardRepository.findById(creditCard1.getId());
        int creditCardRepositoryInitialSize = creditCardRepository.findAll().size();
        assertTrue(creditCard.isPresent());
        creditCard.get().setCreditLimit(new Money(BigDecimal.valueOf(300000), Currency.getInstance("EUR")));
        creditCard.get().setInterestRate(new Money(BigDecimal.valueOf(0.05), Currency.getInstance("EUR")));
        creditCardRepository.save(creditCard.get());
        int creditCardRepositorySizeAfterUpdate = creditCardRepository.findAll().size();
        creditCard = creditCardRepository.findById(creditCard1.getId());
        assertEquals(creditCardRepositoryInitialSize, creditCardRepositorySizeAfterUpdate);
        assertEquals(new Money(BigDecimal.valueOf(10000), Currency.getInstance("EUR")).getAmount(), creditCard.get().getBalance().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(100000), Currency.getInstance("EUR")).getAmount(), creditCard.get().getCreditLimit().getAmount());
        assertEquals(new Money(BigDecimal.valueOf(0.1), Currency.getInstance("EUR")).getAmount(), creditCard.get().getInterestRate().getAmount());
        assertEquals("João Neves", creditCard.get().getPrimaryOwner().getName());
        assertTrue(EncryptionUtil.matches("winter1sComing", creditCard.get().getPrimaryOwner().getPassword()));
        assertEquals(30, creditCard.get().getPrimaryOwner().getAge());
        assertEquals("Rua das Cerejeiras", creditCard.get().getPrimaryOwner().getPrimaryAddress().getAddress());
        assertEquals("1234-432", creditCard.get().getPrimaryOwner().getPrimaryAddress().getPostalCode());
        assertEquals("Viseu", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCity());
        assertEquals("Portugal", creditCard.get().getPrimaryOwner().getPrimaryAddress().getCountry());
    }

    @Test
    void deleteCreditCard_Valid_Deleted(){
        int creditCardRepInitialSize = creditCardRepository.findAll().size();
        creditCardRepository.deleteById(creditCard1.getId());
        int creditCardRepSizeAfterDelete = creditCardRepository.findAll().size();
        assertEquals(creditCardRepInitialSize - 1, creditCardRepSizeAfterDelete);
        creditCardRepository.deleteById(creditCard2.getId());
        int creditCardRepFinalSize = creditCardRepository.findAll().size();
        assertEquals(creditCardRepSizeAfterDelete - 1, creditCardRepFinalSize);
    }
}