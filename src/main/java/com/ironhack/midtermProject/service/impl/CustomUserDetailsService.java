package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.dao.AccountHolder;
import com.ironhack.midtermProject.dao.Admin;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.AdminRepository;
import com.ironhack.midtermProject.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByName(name);
        Optional<AccountHolder> accountHolder = accountHolderRepository.findByName(name);
        if (admin.isEmpty() && accountHolder.isEmpty()){
            throw new UsernameNotFoundException("User not found with username " + name);
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(admin.isPresent() ? admin.get() : accountHolder.get());

        return customUserDetails;
    }
}
