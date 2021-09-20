package com.example.midtermProject.utils;

import com.example.midtermProject.dao.Account;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class EncryptionUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
    private static final PasswordEncoder secretKeyEncoder = new BCryptPasswordEncoder(13);

    public static String encryptedPassword(String plainPassword){
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean matches(String plainPassword, String encryptedPassword){
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }

    public static String getSecretKey(Account account){
        if (account.getPrimaryOwner() != null){
            return secretKeyEncoder.encode(account.getId() +
                    account.getPrimaryOwner().getName() +
                    account.getPrimaryOwner().getPrimaryAddress().getAddress() +
                    account.getPrimaryOwner().getPrimaryAddress().getPostalCode() +
                    account.getPrimaryOwner().getPrimaryAddress().getCity() +
                    account.getPrimaryOwner().getPrimaryAddress().getCountry() +
                    LocalDate.now().toString());
        } else if(account.getId() != null) {
            return secretKeyEncoder.encode(account.getId() +
                    LocalDate.now().toString());
        } else {
            return secretKeyEncoder.encode(LocalDate.now().toString());
        }
    }
}
