package com.ironhack.midtermProject.controller.dto.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

// Class to provide feedback when creating a ThirdParty
public class CreateThirdPartyReceipt {
    private final Long id;
    private final String name;
}
