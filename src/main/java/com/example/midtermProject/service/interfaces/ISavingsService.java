package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.dao.Savings;


public interface ISavingsService {
    void updateBalance(Long id, BalanceDTO balance);
    void createSavings(Savings savings);
}
