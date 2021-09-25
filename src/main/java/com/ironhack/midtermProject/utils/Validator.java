package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.enums.TransactionType;
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
    Generalizer generalizer;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    private void validateThirdParty(int hashedKey) {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);
        if (thirdParty.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no ThirdParty Account with a hashed key " + hashedKey);
        }
    }

    private Account validateTransferAccount(String id, String name, boolean fromThisAccount) {
        Account account;
        try {
            account = generalizer.getAccountFromId(id);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }

        if (fromThisAccount) {
            if (account.getStatus().equals(Status.FROZEN)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The Account with id " +
                        id + " is frozen and cannot do any transactions");
            }
            if (!account.getPrimaryOwner().getName().equalsIgnoreCase(name)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " does not have a primary owner named " + name);
            }
        } else {
            if (account.getSecondaryOwner() == null ? !account.getPrimaryOwner().getName().equalsIgnoreCase(name) :
                    !account.getPrimaryOwner().getName().equalsIgnoreCase(name) && !account.getSecondaryOwner().getName().equalsIgnoreCase(name)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " does not have a owner named " + name);
            }
        }
        return account;
    }

    private Account validateTransferAccount(String id, String secretKey) {
        Account account = generalizer.getAccountFromId(id);
        if (account.getSecretKey().equals(secretKey)){
            return account;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The secretKey provided doesn't match! Unable to transfer");
        }
    }

    private void validateSufficientFunds(Account account, Money transfer) {
        if (!account.hasSufficientFunds(transfer)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account with id " + account.getId() + " doesn't have the necessary funds to transfer");
        }
    }

    private void validatePositiveAmount(Money transfer) {
        if (transfer.getAmount().compareTo(BigDecimal.valueOf(0)) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transaction amount must be positive");
        }
    }

    public List<Account> validateTransaction(String primaryOwnerName, TransactionRequest transactionRequest){
        Account fromAccount;
        Account toAccount;
        try {
            fromAccount = validateTransferAccount(transactionRequest.getFromId(), primaryOwnerName, true);
            toAccount = validateTransferAccount(transactionRequest.getToId(), transactionRequest.getToOwnerName(),false);
            validateSufficientFunds(fromAccount, transactionRequest.getTransfer());
            validatePositiveAmount(transactionRequest.getTransfer());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return List.of(fromAccount, toAccount);
    }
    public Account validateTransaction(int hashedKey, ThirdPartyTransactionRequest transactionRequest){
        Account toAccount;
        try {
            validateThirdParty(hashedKey);
            toAccount = validateTransferAccount(transactionRequest.getToId(), transactionRequest.getSecretKey());
            validateSufficientFunds(toAccount, transactionRequest.getTransfer());
            validatePositiveAmount(transactionRequest.getTransfer());
            validateTransactionType(transactionRequest.getTransactionType());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return toAccount;
    }

    private void validateTransactionType(TransactionType transactionType) {
        if (!transactionType.equals(TransactionType.SEND) && !transactionType.equals(TransactionType.RECEIVE)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transaction types are SEND or RECEIVE. Other transaction types are invalid");
        }
    }

    public CreditCard validateCreditCard(CreditCardDTO creditCardDTO) {
        List<AccountHolder> listOwners = validateOwners(creditCardDTO);
        if (listOwners.size() > 1){
            return new CreditCard(creditCardDTO.getBalance(), listOwners.get(0), listOwners.get(1), creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRate());
        } else {
            return new CreditCard(creditCardDTO.getBalance(), listOwners.get(0), creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRate());
        }
    }

    public Checking validateChecking(CheckingDTO checkingDTO) {
        List<AccountHolder> listOwners = validateOwners(checkingDTO);
        if (listOwners.size() > 1){
            return new Checking(checkingDTO.getBalance(), listOwners.get(0), listOwners.get(1));
        } else {
            return new Checking(checkingDTO.getBalance(), listOwners.get(0));
        }
    }

    public Savings validateSavings(SavingsDTO savingsDTO) {
        List<AccountHolder> listOwners = validateOwners(savingsDTO);
        if (listOwners.size() > 1){
            return new Savings(savingsDTO.getBalance(), listOwners.get(0), listOwners.get(1), savingsDTO.getInterestRate(), savingsDTO.getMinimumBalance());
        } else {
            return new Savings(savingsDTO.getBalance(), listOwners.get(0), savingsDTO.getInterestRate(), savingsDTO.getMinimumBalance());
        }
    }

    public List<AccountHolder> validateOwners(AccountDTO accountDTO) {
        AccountHolder primaryOwner = null;
        AccountHolder secondaryOwner = null;
        try {
            primaryOwner = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).get();
            if (accountDTO.getSecondaryOwnerId() != null){
                secondaryOwner = accountHolderRepository.findById(accountDTO.getSecondaryOwnerId()).get();
            }
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find AccountHolder with id " +
                    (primaryOwner == null ? accountDTO.getPrimaryOwnerId() : accountDTO.getSecondaryOwnerId()));
        }
        if (secondaryOwner != null){
            return List.of(primaryOwner, secondaryOwner);
        } else {
            return List.of(primaryOwner);
        }
    }
}
