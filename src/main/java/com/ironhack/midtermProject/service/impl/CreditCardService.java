package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Transactional
    public void updateBalance(String id, BalanceDTO balance) {
        Optional<CreditCard> storedCreditCard = creditCardRepository.findById(id);
        if (storedCreditCard.isPresent()){
            storedCreditCard.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CreditCard Account not found");
        }
    }

    public void createCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }
}
