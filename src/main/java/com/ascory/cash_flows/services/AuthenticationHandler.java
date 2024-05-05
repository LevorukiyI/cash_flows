package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.JwtRefreshToken;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.JwtRefreshTokenRepository;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationHandler {

    private final JwtService jwtService;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public AuthenticationResponse authenticateAndGetAuthenticationResponse(User user) {
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeUserJwtRefreshToken(user);//TODO изменить логику на multiple devices refresh token
        saveUserRefreshToken(user, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeUserJwtRefreshToken(User user){
        jwtRefreshTokenRepository.deleteIfExistsByUser(user);
    }

    private void saveUserRefreshToken(User user, String jwtToken) {
        var token = JwtRefreshToken.builder()
                .user(user)
                .token(jwtToken)
                .build();
        jwtRefreshTokenRepository.save(token);
    }
}
