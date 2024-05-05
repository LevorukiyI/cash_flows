package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.models.EmailPassVerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailPassVerificationTokenRepository extends JpaRepository<EmailPassVerificationTokenEntity, Long> {
    Optional<EmailPassVerificationTokenEntity> findByToken(String token);
}
