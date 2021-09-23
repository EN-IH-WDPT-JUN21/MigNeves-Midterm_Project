package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    private Money balance;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "penalty_fee_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee_amount"))
    })
    private final Money penaltyFee = new Money(new BigDecimal(40), Currency.getInstance("EUR"), RoundingMode.HALF_EVEN);
    @NotNull(message = "The primary owner is required to create an account!")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @JoinColumn(name = "primary_owner")
    private AccountHolder primaryOwner;

    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @JoinColumn(name = "secondary_owner")
    private AccountHolder secondaryOwner;

    public Account(Money balance, AccountHolder primaryOwner){
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(null);
    }
    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    public void setBalance(Money balance) {
        this.balance = new Money(balance.getAmount(), Currency.getInstance("EUR"));
    }

    public void decreaseBalance(Money money) {
        getBalance().decreaseAmount(money);
    }

    public void increaseBalance(Money money){
        getBalance().increaseAmount(money);
    }
}
