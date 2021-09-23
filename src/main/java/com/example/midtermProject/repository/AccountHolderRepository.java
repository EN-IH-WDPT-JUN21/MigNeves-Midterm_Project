package com.example.midtermProject.repository;

import com.example.midtermProject.dao.AccountHolder;
import com.example.midtermProject.dao.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
    Optional<AccountHolder> findByName(String name);
}
