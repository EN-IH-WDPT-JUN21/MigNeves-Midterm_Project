package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public class CreditCardService implements ICreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Transactional
    public void updateBalance(UUID id, MoneyDTO balance) {
        Optional<CreditCard> storedCreditCard = creditCardRepository.findById(id);
        if (storedCreditCard.isPresent()){
            storedCreditCard.get().setBalance(balance.getMoney());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking Account not found");
        }
    }
}
