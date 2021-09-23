package com.example.midtermProject.service.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.CreditCard;


public interface ICreditCardService {
    void updateBalance(Long id, BalanceDTO balance);
    void createCreditCard(CreditCard creditCard);
}
