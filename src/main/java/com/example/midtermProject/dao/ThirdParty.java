package com.example.midtermProject.dao;

import javax.persistence.Id;

public class ThirdParty extends User{
    @Id
    private Long id;
    private String hashedKey;
    private String name;
}
