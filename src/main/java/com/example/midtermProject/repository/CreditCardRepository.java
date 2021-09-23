package com.example.midtermProject.repository;

import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.queryInterfaces.ICreditCardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    @Query("SELECT c.id AS id, c.balance AS balance, c.creditLimit AS creditLimit, c.interestRate AS interestRate" +
            ", c.penaltyFee AS penaltyFee, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM CreditCard c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.primaryOwner.name = ?1")
    List<ICreditCardInformation> findByPrimaryOwner(String name);

    @Query("SELECT c.id AS id, c.balance AS balance, c.creditLimit AS creditLimit, c.interestRate AS interestRate" +
            ", c.penaltyFee AS penaltyFee, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM CreditCard c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.secondaryOwner.name = ?1")
    List<ICreditCardInformation> findBySecondaryOwner(String name);
}
