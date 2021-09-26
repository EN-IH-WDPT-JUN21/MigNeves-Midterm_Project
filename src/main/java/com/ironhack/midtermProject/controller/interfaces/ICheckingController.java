package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.AccountDTO;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ICheckingController {
    AccountReceipt createChecking(AccountDTO checkingBody);
    List<? extends IStudentCheckingInformation> getPrimaryAccounts(Authentication authentication);
    List<? extends IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication);
}
