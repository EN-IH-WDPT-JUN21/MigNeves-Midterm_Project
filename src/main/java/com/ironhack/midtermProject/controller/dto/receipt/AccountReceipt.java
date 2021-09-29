package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Account;
import com.ironhack.midtermProject.dao.CreditCard;
import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

//Class to provide feedback when creating an Account or when checking an Account's balance
public class AccountReceipt {
    private String id;
    private Money balance;
    private Money penaltyFee;
    private LocalDate creationDate;
    private Status status;
    private String primaryOwnerName;
    private String secondaryOwnerName;

    public AccountReceipt(Account account) {
        setId(account.getId());
        setBalance(account.getBalance());
        setPenaltyFee(account.getPenaltyFee());
        setCreationDate(account.getCreationDate());
        setStatus(account.getStatus());
        setPrimaryOwnerName(account.getPrimaryOwner().getName());
        if (account.getSecondaryOwner() != null) {
            setSecondaryOwnerName(account.getSecondaryOwner().getName());
        }
    }
}
