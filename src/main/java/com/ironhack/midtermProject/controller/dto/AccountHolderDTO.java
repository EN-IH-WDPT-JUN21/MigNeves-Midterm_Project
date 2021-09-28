package com.ironhack.midtermProject.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    private LocalDate birthDate;
    @NotNull
    @Valid
    private AddressDTO primaryAddress;
    private AddressDTO mailingAddress;
}
