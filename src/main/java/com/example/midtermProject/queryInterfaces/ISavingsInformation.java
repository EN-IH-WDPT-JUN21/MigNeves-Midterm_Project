package com.example.midtermProject.queryInterfaces;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ISavingsInformation {
    Long getId();
    Money getBalance();
    Money getMinimumBalance();
    BigDecimal getInterestRate();
    Money getPenaltyFee();
    LocalDate getCreationDate();
    Status getStatus();
    String getPrimaryOwner();
    String getSecondaryOwner();
}
