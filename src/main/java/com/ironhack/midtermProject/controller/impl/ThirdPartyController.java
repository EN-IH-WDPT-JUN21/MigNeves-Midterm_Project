package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.controller.interfaces.IThirdPartyController;
import com.ironhack.midtermProject.service.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/create/third_party")
    @ResponseStatus(HttpStatus.CREATED)
    public void createThirdParty(@RequestBody @Valid ThirdPartyDTO name) {
        thirdPartyService.createThirdParty(name.getName());
    }
}
