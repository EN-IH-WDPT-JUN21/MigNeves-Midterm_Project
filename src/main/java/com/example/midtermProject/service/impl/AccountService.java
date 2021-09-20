package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.dao.*;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Transactional
    public void updateBalance(UUID id, MoneyDTO balance) {
        Optional<Checking> checking = checkingRepository.findById(id);
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(id);
        Optional<Savings> savings = savingsRepository.findById(id);
        Optional<CreditCard> creditCard = creditCardRepository.findById(id);
        if (checking.isEmpty() && studentChecking.isEmpty() && savings.isEmpty() && creditCard.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " not found");
        } else {
            if (checking.isPresent()){
                checking.get().setBalance(balance.getMoney());
            } else if (studentChecking.isPresent()) {
                studentChecking.get().setBalance(balance.getMoney());
            } else if (savings.isPresent()) {
                savings.get().setBalance(balance.getMoney());
            } else {
                creditCard.get().setBalance(balance.getMoney());
            }
        }
    }
}
