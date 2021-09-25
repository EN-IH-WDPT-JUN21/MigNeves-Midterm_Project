package com.ironhack.midtermProject.queryInterfaces;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ISavingsInformation {
    String getId();
    Money getBalance();
    Money getMinimumBalance();
    BigDecimal getInterestRate();
    Money getPenaltyFee();
    LocalDate getCreationDate();
    Status getStatus();
    String getPrimaryOwner();
    String getSecondaryOwner();
}
