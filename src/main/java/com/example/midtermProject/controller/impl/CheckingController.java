package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.controller.interfaces.ICheckingController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    ICheckingService checkingService;

    @GetMapping("/checking-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCheckingBalanceById(@PathVariable("id") UUID id) {
        Optional<Checking> checking = checkingRepository.findById(id);
        return checking.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("/checking-balance/{id}")
    public void updateBalanceById(@PathVariable("id") UUID id, @RequestBody @Valid MoneyDTO balance) {
        checkingService.updateBalance(id, balance);
    }
}
