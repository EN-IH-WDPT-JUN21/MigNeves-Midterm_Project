package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.AccountDTO;
import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.controller.dto.receipt.*;
import com.ironhack.midtermProject.dao.*;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import com.ironhack.midtermProject.repository.*;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.FraudDetector;
import com.ironhack.midtermProject.utils.Generalize;
import com.ironhack.midtermProject.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ThirdPartyTransactionRepository thirdPartyTransactionRepository;

    @Autowired
    Generalize generalizer;

    @Autowired
    Validator validator;

    @Autowired
    FraudDetector fraudDetector;

    @Transactional
    public AccountReceipt updateBalance(String id, BalanceDTO balance) {
        // Get account from id, set new balance and return a receipt of successful balance update otherwise
        // throw ResponseStatusResponse
        try {
            Account account = generalizer.getAccountFromId(id);
            account.setBalance(balance.getBalance());
            return new AccountReceipt(account);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
    }

    @Transactional
    public TransactionReceipt transferMoney(String primaryOwnerName, TransactionRequest transactionRequest) {
        TransactionReceipt transactionReceipt;
        try {
            // Validate transaction and get fromAccount and toAccount
            List<Account> accountList = validator.validateTransaction(primaryOwnerName, transactionRequest);
            Account accountFrom = accountList.get(0);
            Account accountTo = accountList.get(1);
            LocalDateTime currentTransactionDate = LocalDateTime.now();

            // Detect if there is a possible fraud and if not create a Transaction and save it in the repository
            fraudDetector.catchFraud(currentTransactionDate, accountFrom, transactionRequest.getTransfer());
            Transaction transaction = new Transaction(accountFrom, accountTo, transactionRequest.getTransfer(), currentTransactionDate);
            transactionRepository.save(transaction);

            // Process the transaction of money and return a receipt of a successful transaction
            accountFrom.decreaseBalance(transactionRequest.getTransfer());
            accountTo.increaseBalance(transactionRequest.getTransfer());
            transactionReceipt = new TransactionReceipt(transaction);
        } catch (ResponseStatusException e) {
            // ResponseStatusException explaining what went wrong with the transaction
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return transactionReceipt;
    }

    @Transactional
    public ThirdPartyTransactionReceipt transferMoney(int hashedKey, ThirdPartyTransactionRequest transactionRequest) {
        try {
            // Validate transaction and get a new ThirdPartyTransaction
            ThirdPartyTransaction transaction = validator.validateTransaction(hashedKey, transactionRequest);

            // Process transaction and return successful receipt
            if (transactionRequest.getTransactionType().equals(ThirdPartyTransactionType.SEND)) {
                transaction.getToAccount().increaseBalance(transactionRequest.getTransfer());
            } else {
                transaction.getToAccount().decreaseBalance(transactionRequest.getTransfer());
            }
            thirdPartyTransactionRepository.save(transaction);
            return new ThirdPartyTransactionReceipt(transaction);
        } catch (ResponseStatusException e) {
            // ResponseStatusException explaining what went wrong with the transaction
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
    }

    public AccountReceipt createAccount(AccountDTO accountDTO) {
        // If accountType is not provided an error is thrown otherwise after validation an Account is created and a
        // successful receipt is returned
        if (accountDTO.getAccountType() != null) {
            List<AccountHolder> listOwners = validator.validateOwners(accountDTO);
            return processAccountCreation(accountDTO, listOwners);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A AccountType must be provided");
        }
    }

    private AccountReceipt processAccountCreation(AccountDTO accountDTO, List<AccountHolder> listOwners) {
        // Account creation processing. Account created given the AccountType value
        switch (accountDTO.getAccountType()) {
            case SAVINGS:
                Savings savings = new Savings(
                        accountDTO.getBalance(),
                        listOwners.get(0),
                        listOwners.get(1),
                        accountDTO.getInterestRate(),
                        accountDTO.getMinimumBalance());
                savingsRepository.save(savings);
                return new CreateSavingsReceipt(savings);
            case CREDIT_CARD:
                CreditCard creditCard = new CreditCard(
                        accountDTO.getBalance(),
                        listOwners.get(0),
                        listOwners.get(1),
                        accountDTO.getCreditLimit(),
                        accountDTO.getInterestRate());
                creditCardRepository.save(creditCard);
                return new CreateCreditCardReceipt(creditCard);
            case CHECKING:
                if (listOwners.get(0).getAge() < 24) {
                    StudentChecking studentChecking = new StudentChecking(
                            accountDTO.getBalance(),
                            listOwners.get(0),
                            listOwners.get(1));
                    studentCheckingRepository.save(studentChecking);
                    return new AccountReceipt(studentChecking);
                } else {
                    Checking checking = new Checking(
                            accountDTO.getBalance(),
                            listOwners.get(0),
                            listOwners.get(1));
                    checkingRepository.save(checking);
                    return new CreateCheckingReceipt(checking);
                }
            default:
                throw new IllegalArgumentException("There is no AccountType " + accountDTO.getAccountType().toString());
        }
    }
}
