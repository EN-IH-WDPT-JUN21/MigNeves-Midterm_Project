package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.controller.interfaces.IAccountController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.IAccountService;
import com.example.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
public class AccountController implements IAccountController {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    IAccountService accountService;

    @GetMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalanceById(@PathVariable("id") Long id) {
        Optional<? extends Account> account = checkingRepository.findById(id);
        if (account.isPresent()){
            return account.get().getBalance();
        }
        account = studentCheckingRepository.findById(id);
        if (account.isPresent()){
            return account.get().getBalance();
        }
        account = savingsRepository.findById(id);
        if (account.isPresent()){
            return account.get().getBalance();
        }
        account = creditCardRepository.findById(id);
        return account.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalanceById(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        accountService.updateBalance(id, balance);
    }
}
