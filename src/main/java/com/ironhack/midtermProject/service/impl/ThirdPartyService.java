package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.receipt.CreateThirdPartyReceipt;
import com.ironhack.midtermProject.dao.ThirdParty;
import com.ironhack.midtermProject.repository.ThirdPartyRepository;
import com.ironhack.midtermProject.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.abs;

@Service
public class ThirdPartyService implements IThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    public CreateThirdPartyReceipt createThirdParty(String name) {
        // ThirdParty created from name provided
        ThirdParty thirdParty = new ThirdParty(name, getHashKey(name));
        thirdPartyRepository.save(thirdParty);
        return new CreateThirdPartyReceipt(thirdParty.getId(), thirdParty.getName());
    }

    private int getHashKey(String name) {
        // Create HashKey for ThirdParty and prevent repetition
        long count = thirdPartyRepository.count() + 1;
        int hash = abs(Objects.hash(count, name, LocalDateTime.now()));
        while (thirdPartyRepository.findByHashedKey(hash).isPresent()) {
            hash = abs(Objects.hash(count, name, LocalDateTime.now()));
        }
        return hash;
    }
}
