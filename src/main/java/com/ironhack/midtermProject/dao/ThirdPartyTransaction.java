package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.enums.ThirdPartyTransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "transfer_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "transfer_amount"))
    })
    @Valid
    private Money transfer;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ThirdPartyTransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "third_party_id")
    private ThirdParty thirdParty;

    public ThirdPartyTransaction(Account toAccount, Money transfer, ThirdPartyTransactionType transactionType, ThirdParty thirdParty) {
        setToAccount(toAccount);
        setTransfer(new Money(transfer.getAmount(), Currency.getInstance("EUR")));
        setTransactionType(transactionType);
        setThirdParty(thirdParty);
    }
}
