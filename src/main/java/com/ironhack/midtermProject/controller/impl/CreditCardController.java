package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.controller.interfaces.ICreditCardController;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.queryInterfaces.ICreditCardInformation;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.service.interfaces.ICreditCardService;
import com.ironhack.midtermProject.utils.Validator;
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

    @Autowired
    Validator validator;

    @PostMapping("/create/credit_card")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditCard(@RequestBody @Valid CreditCardDTO creditCardBody) {
        CreditCard creditCard = validator.validateCreditCard(creditCardBody);
        creditCardService.createCreditCard(creditCard);
    }
}
