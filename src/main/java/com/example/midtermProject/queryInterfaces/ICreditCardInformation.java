package com.example.midtermProject.queryInterfaces;

import com.example.midtermProject.dao.Money;
import com.example.midtermProject.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ICreditCardInformation {
    Long getId();
    Money getBalance();
    Money getCreditLimit();
    BigDecimal getInterestRate();
    Money getPenaltyFee();
    String getPrimaryOwner();
    String getSecondaryOwner();
}
