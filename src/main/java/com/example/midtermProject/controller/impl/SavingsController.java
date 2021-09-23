package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.controller.interfaces.ISavingsController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.queryInterfaces.ISavingsInformation;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.service.interfaces.ISavingsService;
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

    @GetMapping("/savings/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getSavingsBalanceById(@PathVariable("id") Long id) {
        Optional<Savings> savings = savingsRepository.findById(id);
        return savings.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("/savings/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalanceById(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        savingsService.updateBalance(id, balance);
    }

    @PostMapping("/savings/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavings(@RequestBody @Valid Savings savings) {
        savingsService.createSavings(savings);
    }

    @GetMapping("/savings/primary")
    @ResponseStatus(HttpStatus.OK)
    public List<ISavingsInformation> getPrimaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            return savingsRepository.findByPrimaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/savings/secondary")
    @ResponseStatus(HttpStatus.OK)
    public List<ISavingsInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            return savingsRepository.findBySecondaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
