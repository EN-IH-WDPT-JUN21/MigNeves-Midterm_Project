package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.controller.dto.receipt.CreateThirdPartyReceipt;

public interface IThirdPartyController {
    CreateThirdPartyReceipt createThirdParty(ThirdPartyDTO name);
}
