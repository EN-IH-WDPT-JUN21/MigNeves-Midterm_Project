package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.queryInterfaces.ICheckingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CheckingRepository extends JpaRepository<Checking, String> {
    @Query("SELECT c.id AS id, c.balance AS balance, c.minimumBalance AS minimumBalance, c.monthlyMaintenanceFee AS monthlyMaintenanceFee" +
            ", c.penaltyFee AS penaltyFee, c.creationDate AS creationDate, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Checking c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.primaryOwner.id = ?1")
    List<ICheckingInformation> findByPrimaryOwner(Long id);

    @Query("SELECT c.id AS id, c.balance AS balance, c.minimumBalance AS minimumBalance, c.monthlyMaintenanceFee AS monthlyMaintenanceFee" +
            ", c.penaltyFee AS penaltyFee, c.creationDate AS creationDate, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM Checking c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.secondaryOwner.id = ?1")
    List<ICheckingInformation> findBySecondaryOwner(Long id);
}
