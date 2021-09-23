package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.Checking;

public interface ICheckingService {
    void updateBalance(Long id, BalanceDTO balance);
    void createChecking(Checking checking);
}
