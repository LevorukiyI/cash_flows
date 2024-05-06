package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByDiscordId(String discordId);
    Optional<User> getUserByDiscordId(String discordId);

    boolean existsByGithubId(String githubId);
    Optional<User> getUserByGithubId(String githubId);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    User getUserById(Long id);
    Optional<User> findById(Long id);
}
