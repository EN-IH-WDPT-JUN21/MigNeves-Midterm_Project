package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;

public interface IStudentCheckingService {
    void updateBalance(Long id, BalanceDTO balance);
}
