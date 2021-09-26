package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.TransactionReceipt;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.ThirdPartyTransaction;
import com.ironhack.midtermProject.dao.Transaction;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import com.ironhack.midtermProject.repository.*;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.FraudDetector;
import com.ironhack.midtermProject.utils.Generalizer;
import com.ironhack.midtermProject.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ThirdPartyTransactionRepository thirdPartyTransactionRepository;

    @Autowired
    Generalizer generalizer;

    @Autowired
    Validator validator;

    @Autowired
    FraudDetector fraudDetector;

    @Transactional
    public AccountReceipt updateBalance(String id, BalanceDTO balance) {
        try {
            Account account = generalizer.getAccountFromId(id);
            account.setBalance(balance.getBalance());
            return new AccountReceipt(account);
        } catch (ResponseStatusException e){
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
    }

    @Transactional
    public TransactionReceipt transferMoney(String primaryOwnerName, TransactionRequest transactionRequest){
        TransactionReceipt transactionReceipt;
        try {
            List<Account> accountList = validator.validateTransaction(primaryOwnerName, transactionRequest);
            Account accountFrom = accountList.get(0);
            Account accountTo = accountList.get(1);
            LocalDateTime currentTransactionDate = LocalDateTime.now();
            fraudDetector.catchFraud(currentTransactionDate, accountFrom, transactionRequest.getTransfer());
            Transaction transaction = new Transaction(accountFrom, accountTo, transactionRequest.getTransfer(), currentTransactionDate);
            transactionRepository.save(transaction);
            accountFrom.decreaseBalance(transactionRequest.getTransfer());
            accountTo.increaseBalance(transactionRequest.getTransfer());
            transactionReceipt = new TransactionReceipt(transaction.getId(), accountFrom.getBalance());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
        return transactionReceipt;
    }

    @Transactional
    public ThirdPartyTransactionReceipt transferMoney(int hashedKey, ThirdPartyTransactionRequest transactionRequest){
        try {
            ThirdPartyTransaction transaction = validator.validateTransaction(hashedKey, transactionRequest);
            if (transactionRequest.getTransactionType().equals(ThirdPartyTransactionType.SEND)){
                transaction.getToAccount().increaseBalance(transactionRequest.getTransfer());
            } else {
                transaction.getToAccount().decreaseBalance(transactionRequest.getTransfer());
            }
            thirdPartyTransactionRepository.save(transaction);
            return new ThirdPartyTransactionReceipt(transaction);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
    }
}
