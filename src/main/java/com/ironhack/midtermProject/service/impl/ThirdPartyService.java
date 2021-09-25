package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.dao.ThirdParty;
import com.ironhack.midtermProject.repository.ThirdPartyRepository;
import com.ironhack.midtermProject.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyService implements IThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    public void createThirdParty(String name) {
        ThirdParty thirdParty = new ThirdParty(name);

        thirdPartyRepository.save(thirdParty);
    }
}
