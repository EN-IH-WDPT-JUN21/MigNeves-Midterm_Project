package com.ironhack.midtermProject.queryInterfaces;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;

import java.math.BigDecimal;

public interface ICreditCardInformation {
    String getId();

    Money getBalance();

    Money getCreditLimit();

    BigDecimal getInterestRate();

    Money getPenaltyFee();

    Status getStatus();

    String getPrimaryOwner();

    String getSecondaryOwner();
}
