package com.ironhack.midtermProject.controller.dto.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Class to provide feedback when creating a ThirdParty
public class CreateThirdPartyReceipt {
    private Long id;
    private String name;
}
