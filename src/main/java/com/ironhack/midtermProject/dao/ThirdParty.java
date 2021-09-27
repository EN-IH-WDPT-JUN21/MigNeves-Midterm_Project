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
public class ThirdParty {
    @NotNull
    @Column(unique = true)
    private final int hashedKey;
    @OneToMany(mappedBy = "thirdParty")
    List<ThirdPartyTransaction> transactionList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;

    public ThirdParty() {
        this.hashedKey = hashCode();
    }

    public ThirdParty(String name) {
        setName(name);
        this.hashedKey = hashCode();
    }

    public ThirdParty(String name, int hashedKey) {
        setName(name);
        this.hashedKey = hashedKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
