package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.controller.interfaces.ICreditCardController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.ICreditCardInformation;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
public class CreditCardController implements ICreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    ICreditCardService creditCardService;

    @GetMapping("/credit-card/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCreditCardBalanceById(@PathVariable("id") Long id) {
        Optional<CreditCard> creditCard = creditCardRepository.findById(id);
        return creditCard.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("/credit-card/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalanceById(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        creditCardService.updateBalance(id, balance);
    }

    @PostMapping("/credit-card/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditCard(@RequestBody @Valid CreditCard creditCard) {
        creditCardService.createCreditCard(creditCard);
    }

    @GetMapping("/credit-card/primary")
    @ResponseStatus(HttpStatus.OK)
    public List<ICreditCardInformation> getPrimaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            return creditCardRepository.findByPrimaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/credit-card/secondary")
    @ResponseStatus(HttpStatus.OK)
    public List<ICreditCardInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            return creditCardRepository.findBySecondaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
