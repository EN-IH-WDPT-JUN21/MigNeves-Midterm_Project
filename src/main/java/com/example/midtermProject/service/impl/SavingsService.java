package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.service.interfaces.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public class SavingsService implements ISavingsService {

    @Autowired
    SavingsRepository savingsRepository;

    @Transactional
    public void updateBalance(UUID id, MoneyDTO balance) {
        Optional<Savings> storedSavings = savingsRepository.findById(id);
        if (storedSavings.isPresent()){
            storedSavings.get().setBalance(balance.getMoney());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking Account not found");
        }
    }
}
