package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.queryInterfaces.ICreditCardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    @Query("SELECT c.id AS id, c.balance AS balance, c.creditLimit AS creditLimit, c.interestRate AS interestRate" +
            ", c.penaltyFee AS penaltyFee, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM CreditCard c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.primaryOwner.id = ?1")
    List<ICreditCardInformation> findByPrimaryOwner(Long id);

    @Query("SELECT c.id AS id, c.balance AS balance, c.creditLimit AS creditLimit, c.interestRate AS interestRate" +
            ", c.penaltyFee AS penaltyFee, c.status AS status, c.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM CreditCard c LEFT OUTER JOIN c.secondaryOwner o " +
            "WHERE c.secondaryOwner.id = ?1")
    List<ICreditCardInformation> findBySecondaryOwner(Long id);
}
