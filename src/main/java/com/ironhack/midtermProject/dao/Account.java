package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.midtermProject.utils.CustomPrefixedIdGenerator;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @GenericGenerator(
            name = "account_seq",
            strategy = "com.ironhack.midtermProject.utils.CustomPrefixedIdGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotBlank
    private final String secretKey;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    protected Money balance;
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
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;
    private final LocalDate creationDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "highest_daily_transaction_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "highest_daily_transaction_amount"))
    })
    private Money highestDailyTransaction;

    public Account() {
        this(null, null, null);
    }

    public Account(Money balance, AccountHolder primaryOwner){
        this(balance, primaryOwner, null);
    }
    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
        setStatus(Status.ACTIVE);
        this.creationDate = LocalDate.now();
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public void setBalance(Money balance) {
        if (balance == null){
            this.balance = null;
        } else {
            this.balance = new Money(balance.getAmount().max(BigDecimal.valueOf(0)), Currency.getInstance("EUR"));
        }
    }

    public void decreaseBalance(Money money) {
        if (money != null && money.getAmount() != null) {
            if (money.getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
                getBalance().decreaseAmount(money);
            }
            getBalance().decreaseAmount(money);
        }
    }

    public void increaseBalance(Money money){
        if (money != null && money.getAmount() != null){
            if (money.getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
                getBalance().increaseAmount(money);
            }
        }
    }

    public boolean hasSufficientFunds(Money transfer){
        return getBalance().getAmount().compareTo(transfer.getAmount()) >= 0;
    }

    public void setHighestDailyTransaction(Money highestDailyTransaction) {
        if (highestDailyTransaction != null && highestDailyTransaction.getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
            if (getHighestDailyTransaction() == null) {
                this.highestDailyTransaction = new Money(highestDailyTransaction.getAmount(), Currency.getInstance("EUR"));
            } else {
                this.highestDailyTransaction = new Money(highestDailyTransaction.getAmount().max(getHighestDailyTransaction().getAmount()), Currency.getInstance("EUR"));
            }
        }
    }
}
