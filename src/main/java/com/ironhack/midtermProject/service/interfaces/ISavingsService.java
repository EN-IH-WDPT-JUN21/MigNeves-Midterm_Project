package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateSavingsReceipt;
import com.ironhack.midtermProject.dao.Savings;


public interface ISavingsService {
    void updateBalance(String id, BalanceDTO balance);
    CreateSavingsReceipt createSavings(Savings savings);
}
