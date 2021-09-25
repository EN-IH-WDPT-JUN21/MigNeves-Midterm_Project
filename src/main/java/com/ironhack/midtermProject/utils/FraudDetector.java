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
    public Money catchFraud(LocalDateTime currentTime, Account account, Money transfer) {
        Money highestDailyTransaction = account.getHighestDailyTransaction();
        List<Transaction> transactionList = transactionRepository.findByFromAccount_IdOrderByTransferDateDesc(account.getId());
        if (transactionList.size() != 0){
            Money dailyTransaction = new Money(transfer.getAmount(), Currency.getInstance("EUR"));
            if (transactionList.size() > 5) {
                for (Transaction transaction : transactionList) {
                    if (!transaction.getTransferDate().isBefore(currentTime.with(LocalTime.of(0, 0)))) {
                        dailyTransaction.increaseAmount(transaction.getTransfer());
                    }
                }
                if ((dailyTransaction.getAmount().multiply(BigDecimal.valueOf(1.5))).compareTo(highestDailyTransaction.getAmount()) >= 0) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
                }
            }
            long milliseconds = ChronoUnit.MILLIS.between(transactionList.get(0).getTransferDate(), currentTime);
            if (milliseconds <= 1000){
                account.setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Possible fraud was detected for Account " + account.getId() + "! The Account has been frozen");
            }
            return dailyTransaction;
        } else {
            return transfer;
        }
    }
}
