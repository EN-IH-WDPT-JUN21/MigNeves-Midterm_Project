package com.ironhack.midtermProject.controller.dto;

import com.ironhack.midtermProject.dao.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolderDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotNull
    @Min(value = 18)
    private int age;
    @NotNull
    @Valid
    private AddressDTO primaryAddress;
    private AddressDTO mailingAddress;
}
