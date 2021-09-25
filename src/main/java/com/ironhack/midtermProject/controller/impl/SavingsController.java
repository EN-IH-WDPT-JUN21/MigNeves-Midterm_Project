package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.controller.interfaces.ISavingsController;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.queryInterfaces.ISavingsInformation;
import com.ironhack.midtermProject.repository.SavingsRepository;
import com.ironhack.midtermProject.service.interfaces.ISavingsService;
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
public class SavingsController implements ISavingsController {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    ISavingsService savingsService;

    @Autowired
    Validator validator;

    @PostMapping("/create/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavings(@RequestBody @Valid SavingsDTO savingsDTO) {
        Savings savings = validator.validateSavings(savingsDTO);
        savingsService.createSavings(savings);
    }
}
