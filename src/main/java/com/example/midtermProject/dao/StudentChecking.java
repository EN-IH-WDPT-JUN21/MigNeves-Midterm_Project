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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

@Entity
@Getter
@Setter
public class StudentChecking extends Account {
    @NotBlank
    private final String secretKey;
    private final LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    public StudentChecking(){
        super();
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner){
        super(balance, primaryOwner);
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }
    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
        this.creationDate = LocalDate.now();
        setStatus(Status.ACTIVE);
        this.secretKey = EncryptionUtil.getSecretKey(this);
    }
}
