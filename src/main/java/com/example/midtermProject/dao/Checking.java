package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Status;
import com.example.midtermProject.utils.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Getter
@Setter
public class Checking extends Account {
    @NotBlank
    private final String secretKey;
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
    private final LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public Checking(){
        super();
        this.creationDate = LocalDate.now();
        setMinimumBalance();
        setMonthlyMaintenanceFee();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public Checking(Money balance, AccountHolder primaryOwner){
        super(balance, primaryOwner);
        this.creationDate = LocalDate.now();
        setMinimumBalance();
        setMonthlyMaintenanceFee();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }
    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        this.creationDate = LocalDate.now();
        setMinimumBalance();
        setMonthlyMaintenanceFee();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public void setMonthlyMaintenanceFee() {
        this.monthlyMaintenanceFee = new Money(BigDecimal.valueOf(12), Currency.getInstance("EUR"));
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = new Money(monthlyMaintenanceFee.getAmount(), Currency.getInstance("EUR"));
    }

    public void setMinimumBalance() {
        this.minimumBalance = new Money(BigDecimal.valueOf(250), Currency.getInstance("EUR"));;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = new Money(minimumBalance.getAmount(), Currency.getInstance("EUR"));;
    }

    @Override
    public void decreaseBalance(Money money) {
        if (getBalance().getAmount().subtract(money.getAmount()).compareTo(minimumBalance.getAmount()) < 0){
            money.increaseAmount(getPenaltyFee());
        }
        super.decreaseBalance(money);
    }
}
