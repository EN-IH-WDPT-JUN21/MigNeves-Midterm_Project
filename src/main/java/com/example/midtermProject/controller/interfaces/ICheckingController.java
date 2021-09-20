package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.MoneyDTO;
import com.example.midtermProject.dao.Money;

import java.util.UUID;

public interface ICheckingController {
    Money getCheckingBalanceById(UUID id);
    void updateBalanceById(UUID id, MoneyDTO balance);
}
