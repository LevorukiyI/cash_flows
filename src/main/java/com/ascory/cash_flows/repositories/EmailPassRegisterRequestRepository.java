package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailPassRegisterRequestRepository extends JpaRepository<EmailPassRegisterRequestEntity, Long> {
    Optional<EmailPassRegisterRequestEntity> findByEmail(String email);
}
