package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCheckingReceipt extends AccountReceipt {
    private Money monthlyMaintenanceFee;
    private Money minimumBalance;

    public CreateCheckingReceipt(Checking checking) {
        super(checking);
        setMonthlyMaintenanceFee(checking.getMonthlyMaintenanceFee());
        setMinimumBalance(checking.getMinimumBalance());
    }
}
