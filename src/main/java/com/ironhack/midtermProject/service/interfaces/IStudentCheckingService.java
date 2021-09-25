package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;

public interface IStudentCheckingService {
    void updateBalance(String id, BalanceDTO balance);
}
