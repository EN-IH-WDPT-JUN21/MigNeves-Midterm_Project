package com.ironhack.midtermProject.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Getter
@Setter
public class CreditCard extends Account {
    @Transient
    private static final Money MAX_CREDIT_LIMIT = new Money(BigDecimal.valueOf(100000), Currency.getInstance("EUR"));
    @Transient
    private static final Money MIN_CREDIT_LIMIT = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
    @Transient
    private static final Money DEFAULT_CREDIT_LIMIT = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
    @Transient
    private static final BigDecimal MAX_INTEREST_RATE = BigDecimal.valueOf(0.2);
    @Transient
    private static final BigDecimal MIN_INTEREST_RATE = BigDecimal.valueOf(0.1);
    @Transient
    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(0.1);

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    private Money creditLimit;
    @DecimalMin("0.1")
    @DecimalMax("0.2")
    @Column(precision=5, scale=4)
    private BigDecimal interestRate;
    private LocalDate lastUpdateDate;

    public CreditCard(){
        this(null, null, null, CreditCard.DEFAULT_CREDIT_LIMIT, CreditCard.DEFAULT_INTEREST_RATE);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner){
        this(balance, primaryOwner, null, CreditCard.DEFAULT_CREDIT_LIMIT, CreditCard.DEFAULT_INTEREST_RATE);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        this(balance, primaryOwner, secondaryOwner, CreditCard.DEFAULT_CREDIT_LIMIT, CreditCard.DEFAULT_INTEREST_RATE);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit){
        this(balance, primaryOwner, null, creditLimit, CreditCard.DEFAULT_INTEREST_RATE);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit){
        this(balance, primaryOwner, secondaryOwner, creditLimit, CreditCard.DEFAULT_INTEREST_RATE);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal interestRate){
        this(balance, primaryOwner, null, CreditCard.DEFAULT_CREDIT_LIMIT, interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate){
        this(balance, primaryOwner, secondaryOwner, CreditCard.DEFAULT_CREDIT_LIMIT, interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit, BigDecimal interestRate){
        this(balance, primaryOwner, null, creditLimit, interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate){
        super(null, primaryOwner, secondaryOwner);
        setLastUpdateDate(getCreationDate());
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        setBalance(balance);
    }

    public void setCreditLimit(Money creditLimit) {
        if (creditLimit == null){
            this.creditLimit = CreditCard.DEFAULT_CREDIT_LIMIT;
        } else {
            this.creditLimit = new Money((creditLimit.getAmount().min(CreditCard.MAX_CREDIT_LIMIT.getAmount()))
                    .max(CreditCard.MIN_CREDIT_LIMIT.getAmount()), Currency.getInstance("EUR"));
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            this.interestRate = CreditCard.DEFAULT_INTEREST_RATE;
        } else {
            this.interestRate = (interestRate.min(CreditCard.MAX_INTEREST_RATE)).max(CreditCard.MIN_INTEREST_RATE);
        }
    }

    @Override
    public boolean hasSufficientFunds(Money transfer) {
        return (getBalance().getAmount().add(getCreditLimit().getAmount())).compareTo(transfer.getAmount()) >= 0;
    }

    @Override
    public void setBalance(Money balance) {
        if (balance != null){
            this.balance = new Money(balance.getAmount().max(getCreditLimit().getAmount().negate()), Currency.getInstance("EUR"));
        }
    }
}
