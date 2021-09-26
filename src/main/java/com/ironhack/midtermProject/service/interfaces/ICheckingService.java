package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.dao.Checking;

public interface ICheckingService {
    void updateBalance(String id, BalanceDTO balance);
    AccountReceipt createChecking(Checking checking);
}
