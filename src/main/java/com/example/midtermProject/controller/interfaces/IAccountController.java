package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.dao.Money;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface IAccountController {
    Money getAccountBalanceById(Long id);
    void updateBalanceById(Long id, BalanceDTO balance);
}
