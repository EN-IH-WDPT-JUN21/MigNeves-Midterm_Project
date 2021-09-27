package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.Savings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Class to provide feedback when creating a Savings Account
public class CreateSavingsReceipt extends AccountReceipt {
    private BigDecimal interestRate;
    private Money minimumBalance;

    public CreateSavingsReceipt(Savings savings) {
        super(savings);
        setInterestRate(savings.getInterestRate());
        setMinimumBalance(savings.getMinimumBalance());
    }
}
