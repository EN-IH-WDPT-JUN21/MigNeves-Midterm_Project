package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.enums.Status;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

// TODO - implement interest rate and monthly maintenance fee

@Entity
@Getter
@Setter
public class Checking extends Account {
    @Transient
    private static Money DEFAULT_MINIMUM_BALANCE = new Money(BigDecimal.valueOf(250), Currency.getInstance("EUR"));
    @Transient
    private static Money DEFAULT_MONTHLY_MAINTENANCE_FEE = new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"));
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount"))
    })
    private Money minimumBalance;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount"))
    })
    private Money monthlyMaintenanceFee;
    private LocalDate lastUpdateDate;

    public Checking(){
        this(null, null, null);
        setLastUpdateDate(getCreationDate());
    }

    public Checking(Money balance, AccountHolder primaryOwner){
        this(balance, primaryOwner, null);
        setLastUpdateDate(getCreationDate());
    }
    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        setLastUpdateDate(getCreationDate());
        setMinimumBalance();
        setMonthlyMaintenanceFee();
        setStatus(Status.ACTIVE);
    }

    public void setMonthlyMaintenanceFee() {
        this.monthlyMaintenanceFee = Checking.DEFAULT_MONTHLY_MAINTENANCE_FEE;
    }

    public void setMinimumBalance() {
        this.minimumBalance = Checking.DEFAULT_MINIMUM_BALANCE;
    }

    @Override
    public void decreaseBalance(Money money) {
        if (money != null && money.getAmount() != null){
            if (money.getAmount().compareTo(BigDecimal.valueOf(0)) >= 0) {
                super.decreaseBalance(money);
                if (getBalance().getAmount().compareTo(getMinimumBalance().getAmount()) < 0) {
                    super.decreaseBalance(getPenaltyFee());
                }
            }
        }
    }
}
