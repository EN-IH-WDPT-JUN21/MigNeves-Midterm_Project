package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateAccountHolderReceipt;
import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    public CreateAccountHolderReceipt createAccountHolder(AccountHolderDTO accountHolderDTO) {
        // AccountHolder created only if there is no AccountHolder with the same name, otherwise ResponseStatusException
        if (accountHolderRepository.findByName(accountHolderDTO.getName()).isEmpty()) {
            // Parsing primaryAddress from the DTO
            Address primaryAddress = new Address(accountHolderDTO.getPrimaryAddress().getAddress(),
                    accountHolderDTO.getPrimaryAddress().getCity(), accountHolderDTO.getPrimaryAddress().getCountry(),
                    accountHolderDTO.getPrimaryAddress().getPostalCode());

            // Parsing mailingAddress from DTO if it exists
            Address mailingAddress = null;
            if (accountHolderDTO.getMailingAddress() != null) {
                mailingAddress = new Address(accountHolderDTO.getMailingAddress().getAddress(),
                        accountHolderDTO.getMailingAddress().getCity(), accountHolderDTO.getMailingAddress().getCountry(),
                        accountHolderDTO.getMailingAddress().getPostalCode());
            }
            // Create AccountHolder from DTO info and save in the repository
            AccountHolder accountHolder = new AccountHolder(accountHolderDTO.getName(), accountHolderDTO.getPassword(),
                    accountHolderDTO.getAge(), primaryAddress, mailingAddress);
            accountHolderRepository.save(accountHolder);
            // Return receipt of creation
            return new CreateAccountHolderReceipt(accountHolder);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "There already exists a Account Holder with the provided name");
    }
}
