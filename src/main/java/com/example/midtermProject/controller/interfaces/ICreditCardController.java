package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.dao.Money;

import java.util.UUID;

public interface ICreditCardController {
    Money getCreditCardBalanceById(UUID id);
    Money updateBalanceById(UUID id, Money balance);
}
