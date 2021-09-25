package com.ironhack.midtermProject.utils;

import com.ironhack.midtermProject.dao.Account;
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
        StringBuilder stringToEncrypt = new StringBuilder();
        if (account.getPrimaryOwner() != null) {
            stringToEncrypt.append(account.getPrimaryOwner().toString());
        }
        if (account.getSecondaryOwner() != null) {
            stringToEncrypt.append(account.getSecondaryOwner().toString());
        }
        if (account.getBalance() != null) {
            stringToEncrypt.append(account.getBalance());
        }
        stringToEncrypt.append(LocalDate.now());

        return secretKeyEncoder.encode(stringToEncrypt.toString());
    }
}
