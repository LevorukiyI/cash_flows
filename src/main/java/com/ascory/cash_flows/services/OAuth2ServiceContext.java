package com.ascory.cash_flows.services;

import com.ascory.cash_flows.exceptions.OAuth2AccountAlreadyExistsException;
import com.ascory.cash_flows.exceptions.UserAlreadyExistsException;
import com.ascory.cash_flows.models.*;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceContext{//TODO USE FACTORY

    private OAuth2ServiceStrategy oAuth2ServiceStrategy;
    private final UserRepository userRepository;
    private final AuthenticationHandler authenticationHandler;

    public void setOAuth2ServiceStrategy(OAuth2ServiceStrategy oAuth2ServiceStrategy){
        this.oAuth2ServiceStrategy = oAuth2ServiceStrategy;
    }

    public AuthenticationResponse authenticate(String code) {
        String accessToken = oAuth2ServiceStrategy.getAccessTokenFromAuthenticationService(code);
        String oAuth2Id = oAuth2ServiceStrategy.getOAuth2Id(accessToken);
        User user = oAuth2ServiceStrategy.getUserByOAuth2Id(oAuth2Id);
        return authenticationHandler.authenticateAndGetAuthenticationResponse(user);
    }

    public AuthenticationResponse register(String code) {
        String accessToken = oAuth2ServiceStrategy.getAccessTokenFromRegisterService(code);
        String oAuth2Id = oAuth2ServiceStrategy.getOAuth2Id(accessToken);
        if(oAuth2ServiceStrategy.existsByOAuth2Id(oAuth2Id)){
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        oAuth2ServiceStrategy.setOAuth2IdToUser(user, oAuth2Id);
        userRepository.save(user);
        return authenticationHandler.authenticateAndGetAuthenticationResponse(user);
    }

    public void addVerification(String code, Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("Access is denied. User is unauthenticated or did not provide a JWT token.");
        }
        String accessToken = oAuth2ServiceStrategy.getAccessTokenFromVerificationService(code);
        String oAuth2Id = oAuth2ServiceStrategy.getOAuth2Id(accessToken);
        if(oAuth2ServiceStrategy.existsByOAuth2Id(oAuth2Id)){
            throw new OAuth2AccountAlreadyExistsException("Данный OAuth2 аккаунт уже занят другой учетной записью в нашем сервисе");
        }
        User user = userRepository.getUserById(Long.valueOf(authentication.getName()));
        oAuth2ServiceStrategy.setOAuth2IdToUser(user, oAuth2Id);
        userRepository.save(user);
    }

    public void deleteVerification(Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("Access is denied. User is unauthenticated or did not provide a JWT token.");
        }
        User user = userRepository.getUserById(Long.valueOf(authentication.getName()));
        if(oAuth2ServiceStrategy.checkIsVerificationNotExists(user)) {
            throw new UnsupportedOperationException("Данный способ верификации отсутствует, поэтому его нельзя удалить.");
        }
        oAuth2ServiceStrategy.deleteVerification(user);
        userRepository.save(user);
    }
}
