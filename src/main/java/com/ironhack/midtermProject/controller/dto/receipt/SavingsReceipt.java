package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

// Class to provide feedback when creating a Savings Account
public class SavingsReceipt extends AccountReceipt {
    private BigDecimal interestRate;
    private Money minimumBalance;

    public SavingsReceipt(Savings savings) {
        super(savings);
        setInterestRate(savings.getInterestRate());
        setMinimumBalance(savings.getMinimumBalance());
    }
}
