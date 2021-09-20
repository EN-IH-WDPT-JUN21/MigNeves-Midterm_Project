package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "interest_rate_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "interest_rate_amount"))
    })
    private Money interestRate;

    public CreditCard(){
        super();
        setInterestRate();
        setCreditLimit();
    }

    public CreditCard(Money balance, AccountHolder primaryOwner){
        super(balance, primaryOwner);
        setCreditLimit(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")));
        setInterestRate(new Money(BigDecimal.valueOf(0.2), Currency.getInstance("EUR")));
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")));
        setInterestRate(new Money(BigDecimal.valueOf(0.2), Currency.getInstance("EUR")));
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Money creditLimit, Money interestRate){
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, Money interestRate){
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
        this.interestRate = new Money(BigDecimal.valueOf(0.2), Currency.getInstance("EUR"));
    }

    public void setInterestRate(Money interestRate) {
        this.interestRate = new Money(interestRate.getAmount().max(BigDecimal.valueOf(0.1)), Currency.getInstance("EUR"));
    }
}
