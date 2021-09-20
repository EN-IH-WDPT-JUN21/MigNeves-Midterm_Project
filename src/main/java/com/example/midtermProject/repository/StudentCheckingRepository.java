package com.example.midtermProject.repository;

import com.example.midtermProject.dao.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, UUID> {
}
