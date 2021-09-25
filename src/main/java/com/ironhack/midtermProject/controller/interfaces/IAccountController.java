package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.*;
import com.ironhack.midtermProject.dao.Money;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;


public interface IAccountController {
    Money getAccountBalanceById(String id);
    void updateBalanceById(String id, BalanceDTO balance);
    TransactionReceipt transferToAccountByOwnerAndId(Authentication authentication, TransactionRequest transactionRequest);
    void transferThirdParty(int hashedKey, ThirdPartyTransactionRequest transactionRequest);
    ListOfAccounts getAllAccounts(Authentication authentication);
}
