package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.queryInterfaces.ICheckingInformation;
import com.example.midtermProject.queryInterfaces.ISavingsInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
    @Query("SELECT c.id AS id, c.balance AS balance, c.minimumBalance AS minimumBalance, c.monthlyMaintenanceFee AS monthlyMaintenanceFee" +
            ", c.penaltyFee AS penaltyFee, c.creationDate AS creationDate, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Checking c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.primaryOwner.name = ?1")
    List<ICheckingInformation> findByPrimaryOwner(String name);

    @Query("SELECT c.id AS id, c.balance AS balance, c.minimumBalance AS minimumBalance, c.monthlyMaintenanceFee AS monthlyMaintenanceFee" +
            ", c.penaltyFee AS penaltyFee, c.creationDate AS creationDate, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Checking c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.secondaryOwner.name = ?1")
    List<ICheckingInformation> findBySecondaryOwner(String name);
}
