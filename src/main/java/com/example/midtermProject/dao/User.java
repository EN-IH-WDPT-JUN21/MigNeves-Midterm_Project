package com.example.midtermProject.dao;

import com.example.midtermProject.enums.Role;
import com.example.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public void setPassword(String password) {
        this.password = EncryptionUtil.encryptedPassword(password);
    }

    public User(String name, String password, Role role){
        setName(name);
        setPassword(password);
        setRole(role);
    }
}
