package com.example.midtermProject.controller.interfaces;

import com.example.midtermProject.controller.dto.BalanceDTO;
import com.example.midtermProject.dao.CreditCard;
import com.example.midtermProject.dao.Money;
import com.example.midtermProject.queryInterfaces.ICreditCardInformation;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ICreditCardController {
    Money getCreditCardBalanceById(Long id);
    void updateBalanceById(Long id, BalanceDTO balance);
    void createCreditCard(CreditCard creditCard);
    List<ICreditCardInformation> getPrimaryAccounts(Authentication authentication);
    List<ICreditCardInformation> getSecondaryAccounts(Authentication authentication);
}
