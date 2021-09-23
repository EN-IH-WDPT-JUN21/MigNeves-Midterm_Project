package com.example.midtermProject.repository;

import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.queryInterfaces.ISavingsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SavingsRepository extends JpaRepository<Savings, Long> {

    @Query("SELECT s.id AS id, s.balance AS balance, s.minimumBalance AS minimumBalance, s.interestRate AS interestRate" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Savings s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.primaryOwner.name = ?1")
    List<ISavingsInformation> findByPrimaryOwner(String name);

    @Query("SELECT s.id AS id, s.balance AS balance, s.minimumBalance AS minimumBalance, s.interestRate AS interestRate" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Savings s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.secondaryOwner.name = ?1")
    List<ISavingsInformation> findBySecondaryOwner(String name);
}