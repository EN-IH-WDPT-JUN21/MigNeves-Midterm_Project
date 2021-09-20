package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.controller.interfaces.IStudentCheckingController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.repository.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
public class StudentCheckingController implements IStudentCheckingController {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @GetMapping("/student_checking-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getStudentCheckingBalanceById(@PathVariable("id") UUID id) {
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(id);
        return studentChecking.map(Account::getBalance).orElse(null);
    }
}
