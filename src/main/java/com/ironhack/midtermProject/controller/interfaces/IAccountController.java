package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.controller.dto.receipt.AccountReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.ThirdPartyTransactionReceipt;
import com.ironhack.midtermProject.controller.dto.receipt.TransactionReceipt;
import org.springframework.security.core.Authentication;


public interface IAccountController {
    AccountReceipt getAccountBalanceById(String id);

    AccountReceipt updateBalanceById(String id, BalanceDTO balance);

    TransactionReceipt transferToAccountByOwnerAndId(Authentication authentication, TransactionRequest transactionRequest);

    ThirdPartyTransactionReceipt transferThirdParty(int hashedKey, ThirdPartyTransactionRequest transactionRequest);

    ListOfAccounts getAllAccounts(Authentication authentication);

    AccountReceipt createAccount(AccountDTO accountBody);
}
