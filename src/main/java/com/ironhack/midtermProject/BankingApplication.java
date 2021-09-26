package com.ironhack.midtermProject;

import com.ironhack.midtermProject.utils.AccountUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	AccountUpdater accountUpdater;

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		accountUpdater.updateAccounts();
	}
}