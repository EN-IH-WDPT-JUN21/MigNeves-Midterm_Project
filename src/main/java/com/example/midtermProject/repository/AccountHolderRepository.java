package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
}
