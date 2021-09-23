package com.example.midtermProject.service.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class StudentCheckingService implements IStudentCheckingService {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    public void updateBalance(Long id, BalanceDTO balance) {
        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);
        if (storedStudentChecking.isPresent()){
            storedStudentChecking.get().setBalance(balance.getBalance());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Checking Account not found");
        }
    }
}
