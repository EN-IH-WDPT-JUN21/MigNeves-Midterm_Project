package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.controller.interfaces.IAccountController;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.queryInterfaces.ICheckingInformation;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.Generalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
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
    public Money getAccountBalanceById(@PathVariable("id") String id) {
        Account account = generalizer.getAccountFromId(id);
        return account != null ? account.getBalance() : null;
    }

    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalanceById(@PathVariable("id") String id, @RequestBody @Valid BalanceDTO balance) {
        accountService.updateBalance(id, balance);
    }

    @PatchMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public TransactionReceipt transferToAccountByOwnerAndId(Authentication authentication, @RequestBody @Valid TransactionRequest transactionRequest){
        return accountService.transferMoney(authentication.getName(), transactionRequest);
    }

    @PatchMapping(path = "/transfer/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferThirdParty(@PathVariable("key") int hashedKey, ThirdPartyTransactionRequest transactionRequest){
        accountService.transferMoney(hashedKey, transactionRequest);
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
