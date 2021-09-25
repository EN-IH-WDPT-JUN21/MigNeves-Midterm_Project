package com.ironhack.midtermProject.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
