package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateAccountHolderReceipt;

public interface IAccountHolderService {
    CreateAccountHolderReceipt createAccountHolder(AccountHolderDTO accountHolderDTO);
}
