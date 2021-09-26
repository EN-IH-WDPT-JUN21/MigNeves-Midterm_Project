package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.dao.ThirdPartyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyTransactionRepository extends JpaRepository<ThirdPartyTransaction, Long> {
}
