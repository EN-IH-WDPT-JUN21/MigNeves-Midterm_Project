package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.receipt.CreateThirdPartyReceipt;

public interface IThirdPartyService {
    CreateThirdPartyReceipt createThirdParty(String name);
}
