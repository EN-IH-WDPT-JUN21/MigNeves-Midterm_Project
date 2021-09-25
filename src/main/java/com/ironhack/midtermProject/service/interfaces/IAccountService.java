package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.TransactionReceipt;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;

public interface IAccountService {
    void updateBalance(String id, BalanceDTO balance);
    TransactionReceipt transferMoney(String primaryOwnerName, TransactionRequest transactionRequest);
    void transferMoney(int hashedKey, ThirdPartyTransactionRequest transactionRequest);
}
