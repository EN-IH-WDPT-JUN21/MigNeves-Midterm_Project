package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.controller.interfaces.ICheckingController;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.queryInterfaces.ICheckingInformation;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import com.ironhack.midtermProject.service.interfaces.ICheckingService;
import com.ironhack.midtermProject.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    ICheckingService checkingService;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    Validator validator;

    @PostMapping("/create/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void createChecking(@RequestBody @Valid CheckingDTO checkingBody) {
        Checking checking = validator.validateChecking(checkingBody);
        checkingService.createChecking(checking);
    }

    // TODO - use DTOs instead of entities

    @GetMapping("/primary/checking")
    @ResponseStatus(HttpStatus.OK)
    public List<? extends IStudentCheckingInformation> getPrimaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            if (accountHolder.get().getAge() < 24){
                return studentCheckingRepository.findByPrimaryOwner(accountHolder.get().getId());
            } else {
                return checkingRepository.findByPrimaryOwner(accountHolder.get().getId());
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/secondary/checking")
    @ResponseStatus(HttpStatus.OK)
    public List<ICheckingInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return checkingRepository.findBySecondaryOwner(accountHolder.get().getId());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
