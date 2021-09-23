package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface IStudentCheckingController {
    Money getStudentCheckingBalanceById(Long id);
    void updateBalanceById(Long id, BalanceDTO balance);
    public List<IStudentCheckingInformation> getSecondaryAccounts(Authentication authentication);
}
