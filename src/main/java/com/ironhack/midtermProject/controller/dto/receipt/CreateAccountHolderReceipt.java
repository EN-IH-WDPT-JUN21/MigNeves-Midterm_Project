package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Class to provide feedback when creating a AccountHolder
public class CreateAccountHolderReceipt {
    private Long id;
    private String name;
    private int age;
    private Address primaryAddress;
    private Address mailingAddress;

    public CreateAccountHolderReceipt(AccountHolder accountHolder) {
        setId(accountHolder.getId());
        setName(accountHolder.getName());
        setAge(accountHolder.getAge());
        setPrimaryAddress(accountHolder.getPrimaryAddress());
        setMailingAddress(accountHolder.getMailingAddress());
    }
}
