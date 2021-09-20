package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class CheckingService implements ICheckingService {

    @Autowired
    CheckingRepository checkingRepository;

    @Transactional
    public void updateBalance(UUID id, MoneyDTO balance) {
        Optional<Checking> storedChecking = checkingRepository.findById(id);
        if (storedChecking.isPresent()){
            storedChecking.get().setBalance(balance.getMoney());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking Account not found");
        }
    }
}
