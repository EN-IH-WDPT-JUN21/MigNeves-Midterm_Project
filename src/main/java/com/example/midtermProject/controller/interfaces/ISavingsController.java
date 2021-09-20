package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.dao.Money;

import java.util.UUID;

public interface ISavingsController {
    Money getSavingsBalanceById(UUID id);
}
