package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Checking;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ICheckingController {
    Money getCheckingBalanceById(Long id);
    void updateBalanceById(Long id, BalanceDTO balance);
    void createChecking(Checking checking);
    List<? extends IStudentCheckingInformation> getPrimaryAccounts(Authentication authentication);
    List<? extends IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication);
}
