package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ICheckingController {
    void createChecking(CheckingDTO checkingBody);
    List<? extends IStudentCheckingInformation> getPrimaryAccounts(Authentication authentication);
    List<? extends IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication);
}
