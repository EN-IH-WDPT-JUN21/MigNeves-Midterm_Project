package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class CheckingService implements ICheckingService {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Transactional
    public void updateBalance(Long id, BalanceDTO balance) {
        Optional<Checking> storedChecking = checkingRepository.findById(id);
        if (storedChecking.isPresent()){
            storedChecking.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking Account not found");
        }
    }

    public void createChecking(Checking checking) {
        if (checking.getPrimaryOwner().getAge() < 24){
            StudentChecking studentChecking = new StudentChecking(checking.getBalance(), checking.getPrimaryOwner(), checking.getSecondaryOwner());
            studentCheckingRepository.save(studentChecking);
        } else {
            Checking newChecking = new Checking(checking.getBalance(), checking.getPrimaryOwner(), checking.getSecondaryOwner());
            checkingRepository.save(newChecking);
        }
    }
}
