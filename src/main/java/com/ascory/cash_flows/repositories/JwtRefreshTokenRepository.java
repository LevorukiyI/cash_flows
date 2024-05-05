package com.ascory.authservice.repositories;

import com.ascory.authservice.models.JwtRefreshToken;
import com.ascory.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    @Transactional
    void deleteIfExistsByUser(User user);
}
