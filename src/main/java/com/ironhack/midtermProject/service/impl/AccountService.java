package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.TransactionReceipt;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;
import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Transaction;
import com.ironhack.midtermProject.enums.TransactionType;
import com.ironhack.midtermProject.repository.*;
import com.ironhack.midtermProject.service.interfaces.IAccountService;
import com.ironhack.midtermProject.utils.FraudDetector;
import com.ironhack.midtermProject.utils.Generalizer;
import com.ironhack.midtermProject.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    Generalizer generalizer;

    @Autowired
    Validator validator;

    @Autowired
    FraudDetector fraudDetector;

    @Transactional
    public void updateBalance(String id, BalanceDTO balance) {
        try {
            Account account = generalizer.getAccountFromId(id);
            account.setBalance(balance.getBalance());
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
            Money dailyTransaction = fraudDetector.catchFraud(currentTransactionDate, accountFrom, transactionRequest.getTransfer());
            accountFrom.setHighestDailyTransaction(dailyTransaction);
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
    public void transferMoney(int hashedKey, ThirdPartyTransactionRequest transactionRequest){
        try {
            Account account = validator.validateTransaction(hashedKey, transactionRequest);
            if (transactionRequest.getTransactionType().equals(TransactionType.SEND)){
                account.increaseBalance(transactionRequest.getTransfer());
            } else {
                account.decreaseBalance(transactionRequest.getTransfer());
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }
    }
}
