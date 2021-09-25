package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.StudentChecking;
import com.ironhack.midtermProject.queryInterfaces.IStudentCheckingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, String> {
    @Query("SELECT s.id AS id, s.balance AS balance" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM StudentChecking s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.primaryOwner.id = ?1")
    List<IStudentCheckingInformation> findByPrimaryOwner(Long id);

    @Query("SELECT s.id AS id, s.balance AS balance" +
            ", s.penaltyFee AS penaltyFee, s.creationDate AS creationDate, s.status AS status, s.primaryOwner.name AS primaryOwner" +
            ", o.name AS secondaryOwner FROM StudentChecking s LEFT OUTER JOIN s.secondaryOwner o " +
            "WHERE s.secondaryOwner.id = ?1")
    List<IStudentCheckingInformation> findBySecondaryOwner(Long id);
}
