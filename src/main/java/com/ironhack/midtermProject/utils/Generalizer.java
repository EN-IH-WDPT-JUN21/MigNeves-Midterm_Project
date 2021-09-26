package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.ListOfAccounts;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.repository.SavingsRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class Generalizer {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    public Account getAccountFromId(String id) {
        String idPrefix = id.substring(0,2);
        Optional<? extends Account> account;
        switch (idPrefix){
            case "CH":
                account = checkingRepository.findById(id);
                break;
            case "SC":
                account = studentCheckingRepository.findById(id);
                break;
            case "CC":
                account = creditCardRepository.findById(id);
                break;
            case "SA":
                account = savingsRepository.findById(id);
                break;
            default:
                throw new IllegalArgumentException("There is no Account with id prefix " + idPrefix);
        }
        if (account.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " was not found");
        } else {
            return account.get();
        }
    }

    public ListOfAccounts getAllAccounts(Long id) {
        return new ListOfAccounts(
                savingsRepository.findByPrimaryOwner(id),
                creditCardRepository.findByPrimaryOwner(id),
                checkingRepository.findByPrimaryOwner(id),
                studentCheckingRepository.findByPrimaryOwner(id),
                savingsRepository.findBySecondaryOwner(id),
                creditCardRepository.findBySecondaryOwner(id),
                checkingRepository.findBySecondaryOwner(id),
                studentCheckingRepository.findBySecondaryOwner(id)
        );
    }
}
