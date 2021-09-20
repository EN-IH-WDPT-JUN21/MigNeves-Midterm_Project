package com.example.midtermProject.dao;

import com.example.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    @NotBlank
    private String password;

    public void setPassword(String password) {
        this.password = EncryptionUtil.encryptedPassword(password);
    }

    public User(String password){
        setPassword(password);
    }
}
