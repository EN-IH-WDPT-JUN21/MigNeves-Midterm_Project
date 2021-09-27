package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.AccountDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class Validator {
    @Autowired
    Generalize generalizer;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    // Method to verify if there is a ThirdParty with the provided hashedKey
    private ThirdParty validateThirdParty(int hashedKey) {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);
        if (thirdParty.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no ThirdParty Account with a hashed key " + hashedKey);
        } else {
            return thirdParty.get();
        }
    }

    private Account validateTransferAccount(String id, String name, boolean fromThisAccount) {
        Account account;
        // Verify if there is a Account with the provided
        try {
            account = generalizer.getAccountFromId(id);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }

        // Verify if Account is active, if it is frozen it can not be used to make a transaction
        if (fromThisAccount) {
            if (account.getStatus().equals(Status.FROZEN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The Account with id " +
                        id + " is frozen and cannot do any transactions");
            }
            // verify if the primary owner name matches with the account
            if (!account.getPrimaryOwner().getName().equalsIgnoreCase(name)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " does not have a primary owner named " + name);
            }
        } else {
            // verify if the name matches with any of the account's owners
            if (account.getSecondaryOwner() == null ? !account.getPrimaryOwner().getName().equalsIgnoreCase(name) :
                    !account.getPrimaryOwner().getName().equalsIgnoreCase(name) && !account.getSecondaryOwner().getName().equalsIgnoreCase(name)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " does not have a owner named " + name);
            }
        }
        return account;
    }

    // Verify if account exists and check if provided secret key matches
    private Account validateTransferAccount(String id, String secretKey) {
        Account account = generalizer.getAccountFromId(id);
        if (account.getSecretKey().equals(secretKey)) {
            return account;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The secretKey provided doesn't match! Unable to transfer");
        }
    }

    // Validate if the account has enough funds to do the transaction
    private void validateSufficientFunds(Account account, Money transfer) {
        if (!account.hasSufficientFunds(transfer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account with id " + account.getId() + " doesn't have the necessary funds to transfer");
        }
    }

    // Validate if the amount to transfer is greater than 0
    private void validatePositiveAmount(Money transfer) {
        if (transfer.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transaction amount must be positive");
        }
    }

    public List<Account> validateTransaction(String primaryOwnerName, TransactionRequest transactionRequest) {
        // Verify the account isn't transferring to itself
        if (transactionRequest.getToAccountId().equals(transactionRequest.getFromAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sending and receiving accounts must not be the same!");
        }

        Account fromAccount;
        Account toAccount;
        try {
            // Verify if the Accounts exist and match with the owner name provided and the authentication name
            fromAccount = validateTransferAccount(transactionRequest.getFromAccountId(), primaryOwnerName, true);
            toAccount = validateTransferAccount(transactionRequest.getToAccountId(), transactionRequest.getToOwnerName(), false);
            // Verify the amount to transfer is positive and the Account has sufficient funds
            validatePositiveAmount(transactionRequest.getTransfer());
            validateSufficientFunds(fromAccount, transactionRequest.getTransfer());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return List.of(fromAccount, toAccount);
    }

    public ThirdPartyTransaction validateTransaction(int hashedKey, ThirdPartyTransactionRequest transactionRequest) {
        Account toAccount;
        ThirdParty thirdParty;
        try {
            // Verify if the ThirdParty hashKey exists
            thirdParty = validateThirdParty(hashedKey);
            // Verify if the Account exists and the secretKey matches
            toAccount = validateTransferAccount(transactionRequest.getToAccountId(), transactionRequest.getSecretKey());
            // Verify the amount to transfer is positive and verify if the Account has enough funds
            validatePositiveAmount(transactionRequest.getTransfer());
            if (transactionRequest.getTransactionType() == ThirdPartyTransactionType.RECEIVE) {
                validateSufficientFunds(toAccount, transactionRequest.getTransfer());
            }
            validateTransactionType(transactionRequest.getTransactionType());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return new ThirdPartyTransaction(toAccount, transactionRequest.getTransfer(), transactionRequest.getTransactionType(), thirdParty);
    }

    // Verify if the TransactionType is either SEND or RECEIVE
    private void validateTransactionType(ThirdPartyTransactionType transactionType) {
        if (!transactionType.equals(ThirdPartyTransactionType.SEND) && !transactionType.equals(ThirdPartyTransactionType.RECEIVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transaction types are SEND or RECEIVE. Other transaction types are invalid");
        }
    }

    // Check if AccountHolder(s) exists with the id(s) provided
    public List<AccountHolder> validateOwners(AccountDTO accountDTO) {
        AccountHolder primaryOwner = null;
        AccountHolder secondaryOwner = null;
        try {
            primaryOwner = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).get();
            if (accountDTO.getSecondaryOwnerId() != null) {
                secondaryOwner = accountHolderRepository.findById(accountDTO.getSecondaryOwnerId()).get();
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find AccountHolder with id " +
                    (primaryOwner == null ? accountDTO.getPrimaryOwnerId() : accountDTO.getSecondaryOwnerId()));
        }
        if (secondaryOwner != null && !secondaryOwner.getId().equals(primaryOwner.getId())) {
            return List.of(primaryOwner, secondaryOwner);
        } else {
            return List.of(primaryOwner);
        }
    }
}
