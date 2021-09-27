package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.TransactionReceipt;
import com.ironhack.midtermProject.controller.interfaces.IAccountController;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.Generalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class AccountController implements IAccountController {

    @Autowired
    IAccountService accountService;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    Generalize generalizer;

    // Method for an Admin to access the balance of an Account by id (other information apart from balance are provided)
    @GetMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountReceipt getAccountBalanceById(@PathVariable("id") String id) {
        Account account = generalizer.getAccountFromId(id);
        return new AccountReceipt(account);
    }

    // Method to allow an Admin to change the balance of an Account by id. A balance value out of range defaults to the minimum
    // or maximum allowed balance value
    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountReceipt updateBalanceById(@PathVariable("id") String id, @RequestBody @Valid BalanceDTO balance) {
        return accountService.updateBalance(id, balance);
    }

    // Method to allow a AccountHolder to do a transaction from one of its Accounts
    @PatchMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public TransactionReceipt transferToAccountByOwnerAndId(Authentication authentication, @RequestBody @Valid TransactionRequest transactionRequest) {
        return accountService.transferMoney(authentication.getName(), transactionRequest);
    }

    // Method to allow a ThirdParty to do a transaction with an account
    @PatchMapping(path = "/transfer/{key}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdPartyTransactionReceipt transferThirdParty(@PathVariable("key") int hashedKey, @RequestBody @Valid ThirdPartyTransactionRequest transactionRequest) {
        return accountService.transferMoney(hashedKey, transactionRequest);
    }

    // Method to allow the AccountHolder to observe their Account's information. SecretKey is always hidden.
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public ListOfAccounts getAllAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Account Owner named " + authentication.getName());
            }
            return generalizer.getAllAccounts(accountHolder.get().getId());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    // Method to allow an Admin to create an Account
    @PostMapping("/create/account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountReceipt createAccount(@RequestBody @Valid AccountDTO accountBody) {
        return accountService.createAccount(accountBody);
    }
}
