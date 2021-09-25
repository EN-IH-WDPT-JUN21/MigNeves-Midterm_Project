package com.ironhack.midtermProject.queryInterfaces;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;

import java.time.LocalDate;

public interface IStudentCheckingInformation {
    String getId();
    Money getBalance();
    Money getPenaltyFee();
    LocalDate getCreationDate();
    Status getStatus();
    String getPrimaryOwner();
    String getSecondaryOwner();
}
