package com.example.midtermProject.repository;

import com.example.midtermProject.dao.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, UUID> {
}
