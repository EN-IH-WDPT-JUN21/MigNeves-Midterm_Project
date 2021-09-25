package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.interfaces.IStudentCheckingController;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.StudentChecking;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import com.ironhack.midtermProject.service.interfaces.IStudentCheckingService;
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

    @GetMapping("/secondary/student_checking")
    @ResponseStatus(HttpStatus.OK)
    public List<IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return studentCheckingRepository.findBySecondaryOwner(accountHolder.get().getId());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
