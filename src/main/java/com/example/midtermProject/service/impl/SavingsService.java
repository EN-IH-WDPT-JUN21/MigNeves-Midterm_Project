package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.service.interfaces.ISavingsService;
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
    public void updateBalance(Long id, BalanceDTO balance) {
        Optional<Savings> storedSavings = savingsRepository.findById(id);
        if (storedSavings.isPresent()){
            storedSavings.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings Account not found");
        }
    }

    public void createSavings(Savings savings){
        Savings newSavings = savings.getMinimumBalance() == null || savings.getInterestRate() == null ?
                new Savings(savings.getBalance(), savings.getPrimaryOwner(), savings.getSecondaryOwner()):
                new Savings(savings.getBalance(), savings.getPrimaryOwner(), savings.getSecondaryOwner(),
                        savings.getInterestRate(), savings.getMinimumBalance());
        savingsRepository.save(newSavings);
    }
}
