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
public class StudentChecking extends Account {
    @Id
    private Long id;
    private Money balance;
    private String secretKey;
    private AccountHolder primaryOwner;
    private Optional<AccountHolder> secondaryOwner;
    private Money minimumBalance;
    private Money penaltyFee;
    private LocalDate creationDate;
    private Status status;
    private Money interestRate;
}
