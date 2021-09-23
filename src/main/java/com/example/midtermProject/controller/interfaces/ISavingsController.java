package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Savings;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.ISavingsInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ISavingsController {
    Money getSavingsBalanceById(Long id);
    void updateBalanceById(Long id, BalanceDTO balance);
    void createSavings(Savings savings);
    List<ISavingsInformation> getPrimaryAccounts(Authentication authentication);
    List<ISavingsInformation> getSecondaryAccounts(Authentication authentication);
}
