package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Transaction;
import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

@Component
public class FraudDetector {
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional(dontRollbackOn = ResponseStatusException.class)
    public void catchFraud(LocalDateTime currentTime, Account account, Money transfer) {
        // List of transactions in the last 24 hours
        List<Transaction> transactionList = transactionRepository.findByFromAccount_IdAndTransferDateAfter(account.getId(), currentTime.with(LocalTime.of(0, 0)));
        // List of daily transactions before the last 24 hours
        List<BigDecimal> highestDailyTransactions = transactionRepository.getHighestDailyTransactionBeforeDate(account.getId(), currentTime.with(LocalTime.of(0, 0)));

        // If there were transactions in the last 24 hours identify if any occurred less then a second ago. If so freeze account
        if (transactionList.size() > 0) {
            long milliseconds = ChronoUnit.MILLIS.between(transactionList.get(0).getTransferDate(), currentTime);
            if (milliseconds <= 1000) {
                account.setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
            }
        }

        if (highestDailyTransactions.size() != 0) {
            BigDecimal highestDailyTransaction = highestDailyTransactions.get(0);
            Money dailyTransaction = new Money(transfer.getAmount(), Currency.getInstance("EUR"));
            // Sum transactions in the last 24 hours and the current attempted transaction amount
            for (Transaction transaction : transactionList) {
                if (!transaction.getTransferDate().isBefore(currentTime.with(LocalTime.of(0, 0)))) {
                    dailyTransaction.increaseAmount(transaction.getTransfer());
                } else {
                    break;
                }
            }
            // Only identify 2nd type of fraud after 6 transactions
            // If the transaction amount in the last 24 hours is greater than the highest daily transaction identify has possible fraud and freeze account
            if (transactionRepository.getCountOfTransactionsFromAccountId(account.getId()) >= 5) {
                if ((dailyTransaction.getAmount()).compareTo(highestDailyTransaction.multiply(BigDecimal.valueOf(1.5))) >= 0) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
                }
            }

            // Limit number of transactions in the first transaction day to 5.
        } else if (transactionList.size() == 5) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "First day of transactions limited to 5 transactions");
        }
    }
}
