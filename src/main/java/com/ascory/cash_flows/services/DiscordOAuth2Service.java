package com.ascory.authservice.services;

import com.ascory.authservice.exceptions.DiscordAuthenticationException;
import com.ascory.authservice.exceptions.InvalidAccessTokenException;
import com.ascory.authservice.exceptions.UserNotFoundException;
import com.ascory.authservice.models.OAuth2ServiceName;
import com.ascory.authservice.models.User;
import com.ascory.authservice.repositories.UserRepository;
import com.ascory.authservice.responses.DiscordAccessTokenResponse;
import com.ascory.authservice.responses.DiscordUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DiscordOAuth2Service implements OAuth2ServiceStrategy{
    @Value("${application.security.oauth2.discord.token-url}")
    private String DISCORD_TOKEN_URL;

    @Value("${application.security.oauth2.discord.client-id}")
    private String CLIENT_ID;

    @Value("${application.security.oauth2.discord.client-secret}")
    private String CLIENT_SECRET;

    @Value("${application.security.oauth2.discord.register-redirect-url}")
    private String REGISTER_REDIRECT_URI;

    @Value("${application.security.oauth2.discord.authentication-redirect-url}")
    private String AUTHENTICATION_REDIRECT_URI;

    @Value("${application.security.oauth2.discord.verification-redirect-url}")
    private String VERIFICATION_REDIRECT_URI;

    @Value("${application.security.oauth2.discord.user-url}")
    private String DISCORD_USER_URL;

    private final UserRepository userRepository;

    private String getAccessToken(String code, String redirect_uri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();

        bodyParams.add("client_id", CLIENT_ID);
        bodyParams.add("client_secret", CLIENT_SECRET);
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("redirect_uri", redirect_uri);
        bodyParams.add("scope", "identify email");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<DiscordAccessTokenResponse> responseEntity = restTemplate.exchange(
                DISCORD_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                DiscordAccessTokenResponse.class
        );

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new DiscordAuthenticationException(
                    Objects.requireNonNull(responseEntity.getBody()).getError()
                            + ". "
                            + Objects.requireNonNull(responseEntity.getBody().getErrorDescription())
                            + "."
            );
        }

        return responseEntity.getBody().getAccessToken();
    }

    @Override
    public String getAccessTokenFromRegisterService(String code) {
        return getAccessToken(code, REGISTER_REDIRECT_URI);
    }

    @Override
    public String getAccessTokenFromAuthenticationService(String code) {
        return getAccessToken(code, AUTHENTICATION_REDIRECT_URI);
    }

    @Override
    public String getAccessTokenFromVerificationService(String code) {
        return getAccessToken(code, VERIFICATION_REDIRECT_URI);
    }

    @Override
    public String getOAuth2Id(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<DiscordUserInfoResponse> responseEntity = restTemplate.exchange(
                DISCORD_USER_URL,
                HttpMethod.GET,
                requestEntity,
                DiscordUserInfoResponse.class
        );

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new InvalidAccessTokenException(
                    Objects.requireNonNull(responseEntity.getBody()).getError()
                            + ". "
                            + Objects.requireNonNull(responseEntity.getBody().getErrorDescription())
                            + "."
            );
        }

        return responseEntity.getBody().getId();
    }

    @Override
    public User getUserByOAuth2Id(String oAuth2Id) {
        return userRepository.getUserByDiscordId(oAuth2Id).orElseThrow(() -> new UserNotFoundException("user with such discord id doesn't exists"));
    }

    @Override
    public boolean existsByOAuth2Id(String oAuth2Id) {
        return userRepository.existsByDiscordId(oAuth2Id);
    }

    @Override
    public OAuth2ServiceName getOAuth2ServiceName() {
        return OAuth2ServiceName.DISCORD;
    }

    @Override
    public void setOAuth2IdToUser(User user, String oAuth2Id) {
        user.setDiscordId(oAuth2Id);
    }
}
