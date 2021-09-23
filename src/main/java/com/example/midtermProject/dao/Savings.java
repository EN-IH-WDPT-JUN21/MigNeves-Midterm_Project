package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import com.example.midtermProject.utils.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Savings extends Account {
    @NotBlank
    private final String secretKey;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    private Money minimumBalance;
    private final LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @DecimalMin("0.0")
    @DecimalMax("0.5")
    private BigDecimal interestRate;

    public Savings(){
        super();
        this.creationDate = LocalDate.now();
        setInterestRate();
        setMinimumBalance();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public Savings(Money balance, AccountHolder primaryOwner){
        super(balance, primaryOwner);
        setInterestRate();
        setMinimumBalance();
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate();
        setMinimumBalance();
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, Money minimumBalance){
        super(balance, primaryOwner);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, Money minimumBalance){
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public void setMinimumBalance(){
        this.minimumBalance = new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"));
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = new Money(minimumBalance.getAmount().max(BigDecimal.valueOf(100)), Currency.getInstance("EUR"));
    }

    public void setInterestRate(){
        this.interestRate =BigDecimal.valueOf(0.0025);
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate.min(BigDecimal.valueOf(0.5));
    }

    @Override
    public void decreaseBalance(Money money) {
        if (getBalance().getAmount().subtract(money.getAmount()).compareTo(minimumBalance.getAmount()) < 0){
            money.increaseAmount(getPenaltyFee());
        }
        super.decreaseBalance(money);
    }
}
