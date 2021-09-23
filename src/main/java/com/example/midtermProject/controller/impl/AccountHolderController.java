package com.example.midtermProject.controller.impl;

import com.example.midtermProject.controller.interfaces.IAccountHolderController;
import com.example.midtermProject.dao.Account;
import com.example.midtermProject.repository.CheckingRepository;
import com.example.midtermProject.repository.CreditCardRepository;
import com.example.midtermProject.repository.SavingsRepository;
import com.example.midtermProject.repository.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AccountHolderController implements IAccountHolderController {
}
