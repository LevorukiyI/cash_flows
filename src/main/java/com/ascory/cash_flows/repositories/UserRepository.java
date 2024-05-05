package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByDiscordId(String discordId);
    Optional<User> getUserByDiscordId(String discordId);
    User getUserById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
