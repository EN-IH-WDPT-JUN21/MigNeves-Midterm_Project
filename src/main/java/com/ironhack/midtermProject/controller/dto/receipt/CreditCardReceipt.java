package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

//Class to provide feedback when creating a CreditCard Account
public class CreditCardReceipt extends AccountReceipt {
    private Money creditLimit;
    private BigDecimal interestRate;

    public CreditCardReceipt(CreditCard creditCard) {
        super(creditCard);
        setCreditLimit(creditCard.getCreditLimit());
        setInterestRate(creditCard.getInterestRate());
    }
}
