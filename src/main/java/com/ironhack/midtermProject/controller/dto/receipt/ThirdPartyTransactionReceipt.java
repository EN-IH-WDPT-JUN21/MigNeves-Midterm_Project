package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.dao.ThirdPartyTransaction;
import com.ironhack.midtermProject.dao.Transaction;
import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyTransactionReceipt {
    private Long id;
    private Money transfer;
    private String thirdPartyName;
    private String toAccountId;
    private ThirdPartyTransactionType transactionType;

    public ThirdPartyTransactionReceipt(ThirdPartyTransaction transaction) {
        if (transaction.getThirdParty() != null) {
            setId(transaction.getId());
            setTransfer(transaction.getTransfer());
            setThirdPartyName(transaction.getThirdParty().getName());
            setToAccountId(transaction.getToAccount().getId());
            setTransactionType(transaction.getTransactionType());
        }
    }
}
