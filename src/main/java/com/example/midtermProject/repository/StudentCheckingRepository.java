package com.example.midtermProject.repository;

import com.example.midtermProject.dao.StudentChecking;
import com.example.midtermProject.queryInterfaces.ISavingsInformation;
import com.example.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {
    @Query("SELECT s.id AS id, s.balance AS balance" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM StudentChecking s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.primaryOwner.name = ?1")
    List<IStudentCheckingInformation> findByPrimaryOwner(String name);

    @Query("SELECT s.id AS id, s.balance AS balance" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM StudentChecking s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.secondaryOwner.name = ?1")
    List<IStudentCheckingInformation> findBySecondaryOwner(String name);
}
