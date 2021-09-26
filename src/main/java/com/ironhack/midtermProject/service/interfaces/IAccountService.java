package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.BalanceDTO;
import com.ironhack.midtermProject.controller.dto.ThirdPartyTransactionRequest;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.TransactionReceipt;
import com.ironhack.midtermProject.controller.dto.TransactionRequest;

public interface IAccountService {
    AccountReceipt updateBalance(String id, BalanceDTO balance);
    TransactionReceipt transferMoney(String primaryOwnerName, TransactionRequest transactionRequest);
    ThirdPartyTransactionReceipt transferMoney(int hashedKey, ThirdPartyTransactionRequest transactionRequest);
}
