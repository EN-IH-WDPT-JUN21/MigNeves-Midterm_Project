package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountHolder extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(18)
    private int age;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "primary_address")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_address_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_address_city")),
            @AttributeOverride(name = "country", column = @Column(name = "primary_address_country"))
    })
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "mailing_address")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_address_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "mailing_address_city")),
            @AttributeOverride(name = "country", column = @Column(name = "mailing_address_country"))
    })
    private Address mailingAddress;

    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner")
    private List<Checking> mainChecking;
    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner")
    private List<StudentChecking> mainStudentChecking;
    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner")
    private List<Savings> mainSavings;
    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner")
    private List<CreditCard> mainCreditCard;

    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner")
    private List<Checking> secondaryChecking;
    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner")
    private List<StudentChecking> secondaryStudentChecking;
    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner")
    private List<Savings> secondarySavings;
    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner")
    private List<CreditCard> secondaryCreditCard;

    public AccountHolder( String name, String password, int age, Address primaryAddress){
        super(name, password, Role.ACCOUNT_HOLDER);
        setName(name);
        setAge(age);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(null);
    }

    public AccountHolder(String name, String password, int age, Address primaryAddress, Address mailingAddress){
        super(name, password, Role.ACCOUNT_HOLDER);
        setName(name);
        setAge(age);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }

    @Override
    public String toString() {
        return "AccountHolder{" +
                "age=" + age +
                ", primaryAddress=" + primaryAddress +
                ", mailingAddress=" + mailingAddress +
                '}';
    }
}
