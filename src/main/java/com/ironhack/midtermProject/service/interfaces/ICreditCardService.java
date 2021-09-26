package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateCreditCardReceipt;
import com.ironhack.midtermProject.dao.CreditCard;


public interface ICreditCardService {
    void updateBalance(String id, BalanceDTO balance);
    CreateCreditCardReceipt createCreditCard(CreditCard creditCard);
}
