package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.models.TransactionEntity;
import com.ascory.cash_flows.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findById(Long id);
    @Transactional
    void deleteAllByTransactionPerformer(User user);
}
