package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.interfaces.ICreditCardController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class CreditCardController implements ICreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;

    @GetMapping("/credit_card-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCreditCardBalanceById(@PathVariable("id") UUID id) {
        Optional<CreditCard> creditCard = creditCardRepository.findById(id);
        return creditCard.map(Account::getBalance).orElse(null);
    }

    @Override
    public Money updateBalanceById(UUID id, Money balance) {
        return null;
    }
}
