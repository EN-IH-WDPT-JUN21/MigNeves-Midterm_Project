package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.SavingsDTO;
import com.ironhack.midtermProject.dao.Savings;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.queryInterfaces.ISavingsInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ISavingsController {
    void createSavings(SavingsDTO savingsDTO);
}
