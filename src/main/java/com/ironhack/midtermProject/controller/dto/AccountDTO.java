package com.ironhack.midtermProject.controller.dto;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    @NotNull
    @Valid
    private Money balance;
    @NotNull
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    private Money creditLimit;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    private Money minimumBalance;
    private BigDecimal interestRate;

    public AccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, AccountType accountType) {
        this(balance, primaryOwnerId, secondaryOwnerId, accountType, null, null, null);
    }

    public AccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, AccountType accountType,
                      Money money, BigDecimal bigDecimal) {
        this(balance, primaryOwnerId, secondaryOwnerId, accountType, null, null, null);
        switch (accountType) {
            case CREDIT_CARD:
                setCreditLimit(money);
                setInterestRate(bigDecimal);
                break;
            case SAVINGS:
                setMinimumBalance(money);
                setInterestRate(bigDecimal);
                break;
        }
    }

    public AccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, AccountType accountType,
                      Money creditLimit, Money minimumBalance, BigDecimal interestRate) {
        setBalance(balance);
        setPrimaryOwnerId(primaryOwnerId);
        setSecondaryOwnerId(secondaryOwnerId);
        setAccountType(accountType);
        setCreditLimit(creditLimit);
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
    }
}
