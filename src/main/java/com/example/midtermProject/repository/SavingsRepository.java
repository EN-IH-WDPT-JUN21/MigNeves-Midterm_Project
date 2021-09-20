package com.example.midtermProject.repository;

import com.example.midtermProject.dao.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SavingsRepository extends JpaRepository<Savings, UUID> {
}
