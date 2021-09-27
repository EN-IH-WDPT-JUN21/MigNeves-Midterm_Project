package com.ironhack.midtermProject.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class StudentChecking extends Account {

    public StudentChecking() {
        this(null, null, null);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner) {
        this(balance, primaryOwner, null);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }
}
