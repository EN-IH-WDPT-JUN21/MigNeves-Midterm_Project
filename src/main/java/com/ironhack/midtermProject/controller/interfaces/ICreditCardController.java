package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateCreditCardReceipt;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.queryInterfaces.ICreditCardInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ICreditCardController {
    CreateCreditCardReceipt createCreditCard(CreditCardDTO creditCardBody);
}
