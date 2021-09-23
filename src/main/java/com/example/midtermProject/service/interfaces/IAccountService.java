package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;



public interface IAccountService {
    void updateBalance(Long id, BalanceDTO balance);
}
