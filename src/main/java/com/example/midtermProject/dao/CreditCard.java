package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

@Entity
@Getter
@Setter
public class CreditCard extends Account {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    private Money creditLimit;
    @DecimalMin("0.1")
    private BigDecimal interestRate;

    public CreditCard(){
        super();
        setInterestRate();
        setCreditLimit();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner){
        super(balance, primaryOwner);
        setCreditLimit();
        setInterestRate();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit();
        setInterestRate();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit, BigDecimal interestRate){
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate){
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public void setCreditLimit(){
        this.creditLimit = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = new Money(creditLimit.getAmount().min(BigDecimal.valueOf(100000)), Currency.getInstance("EUR"));
    }

    public void setInterestRate(){
        this.interestRate = BigDecimal.valueOf(0.2);
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate.max(BigDecimal.valueOf(0.1));
    }
}
