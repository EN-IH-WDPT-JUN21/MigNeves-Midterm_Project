package com.ironhack.midtermProject.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ThirdParty{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private final int hashedKey;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "thirdParty")
    List<ThirdPartyTransaction> transactionList;

    public ThirdParty(){
        this.hashedKey = hashCode();
    }

    public ThirdParty(String name){
        setName(name);
        this.hashedKey = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
