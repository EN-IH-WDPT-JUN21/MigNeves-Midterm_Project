package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.CreateCheckingReceipt;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.StudentChecking;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import com.ironhack.midtermProject.service.interfaces.ICheckingService;
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
    public void updateBalance(String id, BalanceDTO balance) {
        Optional<Checking> storedChecking = checkingRepository.findById(id);
        if (storedChecking.isPresent()){
            storedChecking.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking Account not found");
        }
    }

    public AccountReceipt createChecking(Checking checking) {
        if (checking.getPrimaryOwner().getAge() < 24){
            StudentChecking studentChecking = new StudentChecking(checking.getBalance(), checking.getPrimaryOwner(),
                    checking.getSecondaryOwner());
            studentCheckingRepository.save(studentChecking);
            return new AccountReceipt(studentChecking);
        } else {
            checkingRepository.save(checking);
            return new CreateCheckingReceipt(checking);
        }
    }
}
