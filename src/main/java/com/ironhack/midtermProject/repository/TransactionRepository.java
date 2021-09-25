package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTopByFromAccount_IdOrderByTransferDateDesc(String accountId);

    List<Transaction> findByFromAccount_IdOrderByTransferDateDesc(String accountId);
}
