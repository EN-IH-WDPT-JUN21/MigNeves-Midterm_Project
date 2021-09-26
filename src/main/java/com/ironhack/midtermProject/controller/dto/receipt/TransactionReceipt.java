package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReceipt {
    private Long transactionId;
    private Money finalBalance;
}
