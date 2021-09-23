package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.service.interfaces.ICreditCardService;
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
    public void updateBalance(Long id, BalanceDTO balance) {
        Optional<CreditCard> storedCreditCard = creditCardRepository.findById(id);
        if (storedCreditCard.isPresent()){
            storedCreditCard.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CreditCard Account not found");
        }
    }

    public void createCreditCard(CreditCard creditCard) {
        CreditCard newCreditCard = creditCard.getCreditLimit() == null || creditCard.getInterestRate() == null ?
                new CreditCard(creditCard.getBalance(), creditCard.getPrimaryOwner(),
                        creditCard.getSecondaryOwner()) :
                new CreditCard(creditCard.getBalance(), creditCard.getPrimaryOwner(),
                creditCard.getSecondaryOwner(), creditCard.getCreditLimit(), creditCard.getInterestRate());
        creditCardRepository.save(newCreditCard);
    }
}
