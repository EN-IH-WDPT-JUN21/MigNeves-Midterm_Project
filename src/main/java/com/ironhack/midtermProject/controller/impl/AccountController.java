package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.controller.dto.ListOfAccounts;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.TransactionReceipt;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.controller.interfaces.IAccountController;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.Generalizer;
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
    Generalizer generalizer;

    @GetMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountReceipt getAccountBalanceById(@PathVariable("id") String id) {
        Account account = generalizer.getAccountFromId(id);
        return new AccountReceipt(account);
    }

    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AccountReceipt updateBalanceById(@PathVariable("id") String id, @RequestBody @Valid BalanceDTO balance) {
        return accountService.updateBalance(id, balance);
    }

    @PatchMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public TransactionReceipt transferToAccountByOwnerAndId(Authentication authentication, @RequestBody @Valid TransactionRequest transactionRequest){
        return accountService.transferMoney(authentication.getName(), transactionRequest);
    }

    @PatchMapping(path = "/transfer/{key}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdPartyTransactionReceipt transferThirdParty(@PathVariable("key") int hashedKey, @RequestBody @Valid ThirdPartyTransactionRequest transactionRequest){
        return accountService.transferMoney(hashedKey, transactionRequest);
    }

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public ListOfAccounts getAllAccounts(Authentication authentication) {
        if (authentication.isAuthenticated()){
            Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(authentication.getName());
            if (accountHolder.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Account Owner named " + authentication.getName());
            }
            return generalizer.getAllAccounts(accountHolder.get().getId());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
