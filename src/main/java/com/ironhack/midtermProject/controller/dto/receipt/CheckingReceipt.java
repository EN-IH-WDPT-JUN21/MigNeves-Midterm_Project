package com.ironhack.midtermProject.controller.dto.receipt;

import com.ironhack.midtermProject.dao.Checking;
import com.ironhack.midtermProject.dao.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//Class to provide feedback when creating a Checking Account
public class CheckingReceipt extends AccountReceipt {
    private Money monthlyMaintenanceFee;
    private Money minimumBalance;

    public CheckingReceipt(Checking checking) {
        super(checking);
        setMonthlyMaintenanceFee(checking.getMonthlyMaintenanceFee());
        setMinimumBalance(checking.getMinimumBalance());
    }
}
