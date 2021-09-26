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
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

@Component
public class FraudDetector {
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional(dontRollbackOn = ResponseStatusException.class)
    public void catchFraud(LocalDateTime currentTime, Account account, Money transfer) {
        List<Transaction> transactionList = transactionRepository.findByFromAccount_IdAndTransferDateAfter(account.getId(), currentTime.with(LocalTime.of(0,0)));
        List<BigDecimal> highestDailyTransactions = transactionRepository.getHighestDailyTransactionBeforeDate(account.getId(), currentTime.with(LocalTime.of(0,0)));

        if (transactionList.size() > 0) {
            long milliseconds = ChronoUnit.MILLIS.between(transactionList.get(0).getTransferDate(), currentTime);
            if (milliseconds <= 1000) {
                account.setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
            }
        }

        if (highestDailyTransactions.size() != 0) {
            Collections.sort(highestDailyTransactions);
            BigDecimal highestDailyTransaction = highestDailyTransactions.get(highestDailyTransactions.size() - 1);
            Money dailyTransaction = new Money(transfer.getAmount(), Currency.getInstance("EUR"));
            for (Transaction transaction : transactionList) {
                if (!transaction.getTransferDate().isBefore(currentTime.with(LocalTime.of(0, 0)))) {
                    dailyTransaction.increaseAmount(transaction.getTransfer());
                } else {
                    break;
                }
            }
            if (transactionRepository.getCountOfTransactionsFromAccountId(account.getId()) > 5) {
                if ((dailyTransaction.getAmount()).compareTo(highestDailyTransaction.multiply(BigDecimal.valueOf(1.5))) >= 0) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
                }
            }
        } else if (transactionList.size() == 5){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "First day of transactions limited to 5");
        }
    }
}
