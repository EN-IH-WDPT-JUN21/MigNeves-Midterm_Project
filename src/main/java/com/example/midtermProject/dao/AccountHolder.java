package com.example.midtermProject.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountHolder extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
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

    @OneToMany(mappedBy = "primaryOwner")
    private Set<Checking> mainChecking;
    @OneToMany(mappedBy = "primaryOwner")
    private Set<StudentChecking> mainStudentChecking;
    @OneToMany(mappedBy = "primaryOwner")
    private Set<Savings> mainSavings;
    @OneToMany(mappedBy = "primaryOwner")
    private Set<CreditCard> mainCreditCard;

    @OneToMany(mappedBy = "secondaryOwner")
    private Set<Checking> secondaryChecking;
    @OneToMany(mappedBy = "secondaryOwner")
    private Set<StudentChecking> secondaryStudentChecking;
    @OneToMany(mappedBy = "secondaryOwner")
    private Set<Savings> secondarySavings;
    @OneToMany(mappedBy = "secondaryOwner")
    private Set<CreditCard> secondaryCreditCard;

    public AccountHolder( String name, String password, int age, Address primaryAddress){
        super(password);
        setName(name);
        setAge(age);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(null);
    }

    public AccountHolder(String name, String password, int age, Address primaryAddress, Address mailingAddress){
        super(password);
        setName(name);
        setAge(age);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }
}
