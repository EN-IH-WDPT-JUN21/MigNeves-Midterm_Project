package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.ListOfAccounts;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.StudentChecking;
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
public class Generalize {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    AccountUpdater accountUpdater;

    // Method to get account from id
    // Processes the id prefix to search in the correct repository
    public Account getAccountFromId(String id) {
        String idPrefix = id.substring(0, 2);
        Optional<? extends Account> account;
        switch (idPrefix) {
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
            if (account.get().getClass() != StudentChecking.class) {
                accountUpdater.updateAccount(account.get());
            }
            return account.get();
        }
    }

    // Method to get account by id if the AccountHolder is the owner
    public Account getAccountFromIdAndName(String id, String name) {
        Account account = getAccountFromId(id);
        if (!account.getPrimaryOwner().getName().equals(name) && (account.getSecondaryOwner() == null || !account.getSecondaryOwner().getName().equals(name))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The account with id " + account.getId() + " is not owned by " + name);
        } else {
            return account;
        }
    }

    // get all accounts from user id
    public ListOfAccounts getAllAccounts(Long id) {
        accountUpdater.updateAccounts();
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
