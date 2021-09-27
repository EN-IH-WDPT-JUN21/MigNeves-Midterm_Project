package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateAccountHolderReceipt;
import com.ironhack.midtermProject.controller.interfaces.IAccountHolderController;
import com.ironhack.midtermProject.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    IAccountHolderService accountHolderService;

    // Method to allow the creation of a new AccountHolder. Similar to a SignUp. No authentication required
    @PostMapping("/create/user")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccountHolderReceipt createAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        return accountHolderService.createAccountHolder(accountHolderDTO);
    }
}
