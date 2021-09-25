package com.ironhack.midtermProject.controller.dto;

import com.ironhack.midtermProject.dao.Money;
import com.ironhack.midtermProject.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyTransactionRequest {
    @NotBlank
    private String toId;
    @NotNull
    private String secretKey;
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount"))
    })
    @Valid
    private Money transfer;
    @NotBlank
    @Enumerated(EnumType.STRING)
    TransactionType transactionType;
}
