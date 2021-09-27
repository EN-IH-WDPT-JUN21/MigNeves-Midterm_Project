package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.transfer.amount) FROM Transaction t WHERE t.fromAccount.id = ?1 AND t.transferDate < ?2 GROUP BY t.fromAccount.id, CONVERT(t.transferDate AS DATE) ORDER BY SUM(t.transfer.amount) DESC")
    List<BigDecimal> getHighestDailyTransactionBeforeDate(String id, LocalDateTime localDateTime);

    List<Transaction> findByFromAccount_IdAndTransferDateAfter(String accountId, LocalDateTime localDateTime);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.fromAccount.id =?1")
    int getCountOfTransactionsFromAccountId(String accountId);
}
