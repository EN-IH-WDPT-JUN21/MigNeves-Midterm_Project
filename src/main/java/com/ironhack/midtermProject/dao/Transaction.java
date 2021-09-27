package com.ironhack.midtermProject.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Getter
@Setter
public class Transaction {
    private final LocalDateTime transferDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "transfer_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "transfer_amount"))
    })
    private Money transfer;

    public Transaction() {
        transferDate = LocalDateTime.now();
    }

    public Transaction(Account fromAccount, Account toAccount, Money transfer, LocalDateTime transferDate) {
        setFromAccount(fromAccount);
        setToAccount(toAccount);
        setTransfer(new Money(transfer.getAmount(), Currency.getInstance("EUR")));
        this.transferDate = transferDate;
    }
}
