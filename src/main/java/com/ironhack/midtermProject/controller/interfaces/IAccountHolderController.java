package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateAccountHolderReceipt;

public interface IAccountHolderController {
    CreateAccountHolderReceipt createAccountHolder(AccountHolderDTO accountHolderDTO);
}
