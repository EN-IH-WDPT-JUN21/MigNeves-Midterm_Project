package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateSavingsReceipt;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.repository.SavingsRepository;
import com.ironhack.midtermProject.service.interfaces.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SavingsService implements ISavingsService {

    @Autowired
    SavingsRepository savingsRepository;

    @Transactional
    public void updateBalance(String id, BalanceDTO balance) {
        Optional<Savings> storedSavings = savingsRepository.findById(id);
        if (storedSavings.isPresent()){
            storedSavings.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings Account not found");
        }
    }

    public CreateSavingsReceipt createSavings(Savings savings){
        savingsRepository.save(savings);
        return new CreateSavingsReceipt(savings);
    }
}
