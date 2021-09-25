package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.queryInterfaces.ISavingsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SavingsRepository extends JpaRepository<Savings, String> {

    @Query("SELECT s.id AS id, s.balance AS balance, s.minimumBalance AS minimumBalance, s.interestRate AS interestRate" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Savings s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.primaryOwner.id = ?1")
    List<ISavingsInformation> findByPrimaryOwner(Long id);

    @Query("SELECT s.id AS id, s.balance AS balance, s.minimumBalance AS minimumBalance, s.interestRate AS interestRate" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Savings s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.secondaryOwner.id = ?1")
    List<ISavingsInformation> findBySecondaryOwner(Long id);
}