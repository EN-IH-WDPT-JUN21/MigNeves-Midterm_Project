package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.controller.interfaces.ICheckingController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.ICheckingInformation;
import com.example.midtermProject.queryInterfaces.IStudentCheckingInformation;
import com.example.midtermProject.repository.AccountHolderRepository;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import com.example.midtermProject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    @GetMapping("/checking/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCheckingBalanceById(@PathVariable("id") Long id) {
        Optional<Checking> checking = checkingRepository.findById(id);
        return checking.map(Account::getBalance).orElse(null);
    }

    @PatchMapping("/checking/balance/{id}")
    public void updateBalanceById(@PathVariable("id") Long id, @RequestBody @Valid BalanceDTO balance) {
        checkingService.updateBalance(id, balance);
    }

    @PostMapping("/checking/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createChecking(@RequestBody @Valid Checking checking) {
        checkingService.createChecking(checking);
    }

    @GetMapping("/checking/primary")
    @ResponseStatus(HttpStatus.OK)
    public List<? extends IStudentCheckingInformation> getPrimaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            if (accountHolder.get().getAge() < 24){
                return studentCheckingRepository.findByPrimaryOwner(authentication.getName());
            } else {
                return checkingRepository.findByPrimaryOwner(authentication.getName());
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/checking/secondary")
    @ResponseStatus(HttpStatus.OK)
    public List<ICheckingInformation> getSecondaryAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return checkingRepository.findBySecondaryOwner(authentication.getName());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
