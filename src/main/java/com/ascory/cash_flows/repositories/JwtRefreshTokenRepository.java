package com.ascory.cash_flows.repositories;

import com.ascory.cash_flows.models.JwtRefreshToken;
import com.ascory.cash_flows.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    @Transactional
    void deleteIfExistsByUser(User user);
}
