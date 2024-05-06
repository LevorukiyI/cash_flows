package com.ascory.cash_flows.services;

import com.ascory.cash_flows.exceptions.DiscordAuthenticationException;
import com.ascory.cash_flows.exceptions.InvalidAccessTokenException;
import com.ascory.cash_flows.exceptions.UserNotFoundException;
import com.ascory.cash_flows.models.OAuth2ServiceName;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.responses.GithubAccessTokenResponse;
import com.ascory.cash_flows.responses.GithubUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GitHubOAuth2Service implements OAuth2ServiceStrategy{
    private final UserRepository userRepository;
    @Value("${application.security.oauth2.github.register.client-id}")
    private String REGISTER_CLIENT_ID;

    @Value("${application.security.oauth2.github.register.client-secret}")
    private String REGISTER_CLIENT_SECRET;

    @Value("${application.security.oauth2.github.login.client-id}")
    private String LOGIN_CLIENT_ID;

    @Value("${application.security.oauth2.github.login.client-secret}")
    private String LOGIN_CLIENT_SECRET;

    @Value("${application.security.oauth2.github.verification.client-id}")
    private String VERIFY_CLIENT_ID;

    @Value("${application.security.oauth2.github.verification.client-secret}")
    private String VERIFY_CLIENT_SECRET;

    @Value("${application.security.oauth2.github.access-token-url}")
    private String GITHUB_TOKEN_URL;

    @Value("${application.security.oauth2.github.user-url}")
    private String GITHUB_USER_URL;

    @Value("${application.security.oauth2.github.register.redirect-url}")
    private String REGISTER_REDIRECT_URI;

    @Value("${application.security.oauth2.github.login.redirect-url}")
    private String AUTHENTICATION_REDIRECT_URI;

    @Value("${application.security.oauth2.github.verification.redirect-url}")
    private String VERIFICATION_REDIRECT_URI;

    private String getAccessToken(String code, String redirect_uri, String client_id, String client_secret) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();

        bodyParams.add("client_id", client_id);
        bodyParams.add("client_secret", client_secret);
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("redirect_uri", redirect_uri);
        bodyParams.add("scope", "identify email");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<GithubAccessTokenResponse> responseEntity = restTemplate.exchange(
                GITHUB_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                GithubAccessTokenResponse.class
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
        return getAccessToken(code, REGISTER_REDIRECT_URI, REGISTER_CLIENT_ID, REGISTER_CLIENT_SECRET);
    }

    @Override
    public String getAccessTokenFromAuthenticationService(String code) {
        return getAccessToken(code, AUTHENTICATION_REDIRECT_URI, LOGIN_CLIENT_ID, LOGIN_CLIENT_SECRET);
    }

    @Override
    public String getAccessTokenFromVerificationService(String code) {
        return getAccessToken(code, VERIFICATION_REDIRECT_URI, VERIFY_CLIENT_ID, VERIFY_CLIENT_SECRET);
    }

    @Override
    public String getOAuth2Id(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<GithubUserInfoResponse> responseEntity = restTemplate.exchange(
                GITHUB_USER_URL,
                HttpMethod.GET,
                requestEntity,
                GithubUserInfoResponse.class
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
        return userRepository.getUserByGithubId(oAuth2Id).orElseThrow(() -> new UserNotFoundException("user with such github id doesn't exists"));
    }

    @Override
    public boolean existsByOAuth2Id(String oAuth2Id) {
        return userRepository.existsByGithubId(oAuth2Id);
    }

    @Override
    public OAuth2ServiceName getOAuth2ServiceName() {
        return OAuth2ServiceName.GITHUB;
    }

    @Override
    public void setOAuth2IdToUser(User user, String oAuth2Id) {
        user.setGithubId(oAuth2Id);
    }

    @Override
    public boolean checkIsVerificationNotExists(User user) {
        return user.getGithubId() == null;
    }

    @Override
    public void deleteVerification(User user) {
        user.setGithubId(null);
    }
}
