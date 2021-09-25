package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.utils.EncryptionUtil;
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
public class Savings extends Account {
    @Transient
    private static final Money MAX_MINIMUM_BALANCE = new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"));
    @Transient
    private static final Money MIN_MINIMUM_BALANCE = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
    @Transient
    private static final Money DEFAULT_MINIMUM_BALANCE = new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"));
    @Transient
    private static final BigDecimal MAX_INTEREST_RATE = BigDecimal.valueOf(0.5);
    @Transient
    private static final BigDecimal MIN_INTEREST_RATE = BigDecimal.valueOf(0);
    @Transient
    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(0.0025);

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    private Money minimumBalance;
    @DecimalMin("0.0")
    @DecimalMax("0.5")
    @Column(precision=5, scale=4)
    private BigDecimal interestRate;
    private LocalDate lastUpdateDate;

    public Savings(){
        this(null, null, null, Savings.DEFAULT_INTEREST_RATE, Savings.DEFAULT_MINIMUM_BALANCE);
    }

    public Savings(Money balance, AccountHolder primaryOwner){
        this(balance, primaryOwner, null, Savings.DEFAULT_INTEREST_RATE, Savings.DEFAULT_MINIMUM_BALANCE);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        this(balance, primaryOwner, secondaryOwner, Savings.DEFAULT_INTEREST_RATE, Savings.DEFAULT_MINIMUM_BALANCE);
    }

    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate){
        this(balance, primaryOwner, null, interestRate, Savings.DEFAULT_MINIMUM_BALANCE);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate){
        this(balance, primaryOwner, secondaryOwner, interestRate, Savings.DEFAULT_MINIMUM_BALANCE);
    }

    public Savings(Money balance, AccountHolder primaryOwner, Money minimumBalance){
        this(balance, primaryOwner, null, Savings.DEFAULT_INTEREST_RATE, minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money minimumBalance){
        this(balance, primaryOwner, secondaryOwner, Savings.DEFAULT_INTEREST_RATE, minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, Money minimumBalance){
        this(balance, primaryOwner, null, interestRate, minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, Money minimumBalance){
        super(balance, primaryOwner, secondaryOwner);
        setLastUpdateDate(getCreationDate());
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
    }

    public void setMinimumBalance(Money minimumBalance) {
        if (minimumBalance == null) {
            this.minimumBalance = Savings.DEFAULT_MINIMUM_BALANCE;
        } else {
            this.minimumBalance = new Money((minimumBalance.getAmount().min(Savings.MAX_MINIMUM_BALANCE.getAmount()))
                    .max(Savings.MIN_MINIMUM_BALANCE.getAmount()), Currency.getInstance("EUR"));
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            this.interestRate = Savings.DEFAULT_INTEREST_RATE;
        } else {
            this.interestRate = (interestRate.min(Savings.MAX_INTEREST_RATE)).max(Savings.MIN_INTEREST_RATE);
        }
    }

    @Override
    public void decreaseBalance(Money money) {
        if (money != null && money.getAmount() != null) {
            if (money.getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
                if (getBalance().getAmount().subtract(money.getAmount()).compareTo(minimumBalance.getAmount()) < 0){
                    money.increaseAmount(getPenaltyFee());
                }
                super.decreaseBalance(money);
            }
        }
    }
}
