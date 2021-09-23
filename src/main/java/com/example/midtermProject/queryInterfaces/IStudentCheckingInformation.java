package com.example.midtermProject.queryInterfaces;

import com.example.midtermProject.dao.Money;
import com.example.midtermProject.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IStudentCheckingInformation {
    Long getId();
    Money getBalance();
    Money getPenaltyFee();
    LocalDate getCreationDate();
    Status getStatus();
    String getPrimaryOwner();
    String getSecondaryOwner();
}
