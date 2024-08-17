package com.ascory.cash_flows.services;

import com.ascory.cash_flows.exceptions.DiscordAuthenticationException;
import com.ascory.cash_flows.exceptions.InvalidAccessTokenException;
import com.ascory.cash_flows.exceptions.UserNotFoundException;
import com.ascory.cash_flows.models.OAuth2ServiceName;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.UserRepository;
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
public class GoogleOAuth2Service implements OAuth2ServiceStrategy{
    @Value("${application.security.oauth2.google.token-url}")
    private String GOOGLE_TOKEN_URL;

    @Value("${application.security.oauth2.google.client-id}")
    private String CLIENT_ID;

    @Value("${application.security.oauth2.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${application.security.oauth2.google.register-redirect-url}")
    private String REGISTER_REDIRECT_URI;

    @Value("${application.security.oauth2.google.authentication-redirect-url}")
    private String AUTHENTICATION_REDIRECT_URI;

    @Value("${application.security.oauth2.google.verification-redirect-url}")
    private String VERIFICATION_REDIRECT_URI;

    @Value("${application.security.oauth2.google.user-url}")
    private String GOOGLE_USER_URL;

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

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        ResponseEntity<GoogleAccessTokenResponse> responseEntity = restTemplate.exchange(
                GOOGLE_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                GoogleAccessTokenResponse.class
        );

        if (!responseEntity.getStatusCode().is2xxSuccessful() || !responseEntity.hasBody()) {
            String errorMessage = responseEntity.getBody() != null
                    ? responseEntity.getBody().getError() + ": " + responseEntity.getBody().getErrorDescription()
                    : "Ошибка при получении токена.";

            throw new DiscordAuthenticationException(errorMessage);
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

        ResponseEntity<GoogleUserInfoResponse> responseEntity = restTemplate.exchange(
                GOOGLE_USER_URL,
                HttpMethod.GET,
                requestEntity,
                GoogleUserInfoResponse.class
        );

        System.out.println(requestEntity.getBody());

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
        return userRepository.getUserByGoogleId(oAuth2Id).orElseThrow(() -> new UserNotFoundException("user with such google id doesn't exists"));
    }

    @Override
    public boolean existsByOAuth2Id(String oAuth2Id) {
        return userRepository.existsByGoogleId(oAuth2Id);
    }

    @Override
    public OAuth2ServiceName getOAuth2ServiceName() {
        return OAuth2ServiceName.GOOGLE;
    }

    @Override
    public void setOAuth2IdToUser(User user, String oAuth2Id) {
        user.setGoogleId(oAuth2Id);
    }

    @Override
    public boolean checkIsVerificationNotExists(User user) {
        return user.getGoogleId() == null;
    }

    @Override
    public void deleteVerification(User user) {
        user.setGoogleId(null);
    }
}

