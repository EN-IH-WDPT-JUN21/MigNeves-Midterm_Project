package com.example.midtermProject.dao;

import javax.persistence.Id;
import java.util.Optional;

public class AccountHolder extends User {
    @Id
    private Long id;
    private String name;
    private Address primaryAddress;
    private Optional<Address> mailingAddress;
}
