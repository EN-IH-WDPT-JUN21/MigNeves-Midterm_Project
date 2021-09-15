package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard extends Account {
    @Id
    private Long id;
    private Money balance;
    private AccountHolder primaryOwner;
    private Optional<AccountHolder> secondaryOwner;
    private Money creditLimit;
    private Money penaltyFee;
    private Money interestRate;
}
