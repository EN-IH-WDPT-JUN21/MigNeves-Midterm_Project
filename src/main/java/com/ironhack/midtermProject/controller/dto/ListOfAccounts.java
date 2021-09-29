package com.ironhack.midtermProject.controller.dto;

import com.ironhack.midtermProject.queryInterfaces.ICheckingInformation;
import com.ironhack.midtermProject.queryInterfaces.ICreditCardInformation;
import com.ironhack.midtermProject.queryInterfaces.ISavingsInformation;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListOfAccounts {
    List<ISavingsInformation> primarySavingsAccounts;
    List<ICreditCardInformation> primaryCreditCardAccounts;
    List<ICheckingInformation> primaryCheckingAccounts;
    List<IStudentCheckingInformation> primaryStudentCheckingAccounts;
    List<ISavingsInformation> secondarySavingsAccounts;
    List<ICreditCardInformation> secondaryCreditCardAccounts;
    List<ICheckingInformation> secondaryCheckingAccounts;
    List<IStudentCheckingInformation> secondaryStudentCheckingAccounts;
}
