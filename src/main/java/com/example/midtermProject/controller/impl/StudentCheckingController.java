package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.controller.interfaces.IStudentCheckingController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.queryInterfaces.IStudentCheckingInformation;
import com.example.midtermProject.repository.AccountHolderRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
public class StudentCheckingController implements IStudentCheckingController {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    IStudentCheckingService studentCheckingService;

    @GetMapping("/student-checking/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getStudentCheckingBalanceById(@PathVariable("id") Long id) {
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(id);
        return studentChecking.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("student-checking/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalanceById(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        studentCheckingService.updateBalance(id, balance);
    }

    @GetMapping("/student-checking/secondary")
    @ResponseStatus(HttpStatus.OK)
    public List<IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return studentCheckingRepository.findBySecondaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
