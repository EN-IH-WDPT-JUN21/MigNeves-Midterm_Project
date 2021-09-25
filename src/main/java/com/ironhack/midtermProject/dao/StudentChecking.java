package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.utils.CustomPrefixedIdGenerator;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class StudentChecking extends Account {

    public StudentChecking(){
        this(null, null ,null);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner){
        this(balance, primaryOwner, null);
    }
    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        super(balance, primaryOwner, secondaryOwner);
    }
}
