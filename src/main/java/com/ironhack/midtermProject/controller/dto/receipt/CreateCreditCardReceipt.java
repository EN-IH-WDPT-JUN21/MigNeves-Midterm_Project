package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardReceipt extends AccountReceipt {
    private Money creditLimit;
    private BigDecimal interestRate;

    public CreateCreditCardReceipt(CreditCard creditCard) {
        super(creditCard);
        setCreditLimit(creditCard.getCreditLimit());
        setInterestRate(creditCard.getInterestRate());
    }
}
