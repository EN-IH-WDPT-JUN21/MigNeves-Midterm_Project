package com.ironhack.midtermProject.dao;

import com.ironhack.midtermProject.enums.Role;
import com.ironhack.midtermProject.utils.EncryptionUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
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

    public User(String name, String password, Role role) {
        setName(name);
        setPassword(password);
        setRole(role);
    }

    public void setPassword(String password) {
        this.password = EncryptionUtil.encryptedPassword(password);
    }
}
