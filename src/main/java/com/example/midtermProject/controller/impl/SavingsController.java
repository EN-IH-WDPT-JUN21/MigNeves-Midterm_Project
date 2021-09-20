package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.interfaces.ISavingsController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.repository.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class SavingsController implements ISavingsController {

    @Autowired
    SavingsRepository savingsRepository;

    @GetMapping("/savings-balance/{id}")
    public Money getSavingsBalanceById(@PathVariable("id") UUID id) {
        Optional<Savings> savings = savingsRepository.findById(id);
        return savings.map(Account::getBalance).orElse(null);
    }
}
