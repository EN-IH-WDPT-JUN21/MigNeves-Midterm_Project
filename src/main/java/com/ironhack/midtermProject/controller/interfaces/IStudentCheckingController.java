package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface IStudentCheckingController {
    public List<IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication);
}
