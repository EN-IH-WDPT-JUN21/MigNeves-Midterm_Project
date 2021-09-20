package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.MoneyDTO;

import java.util.UUID;

public interface IAccountService {
    void updateBalance(UUID id, MoneyDTO balance);
}
