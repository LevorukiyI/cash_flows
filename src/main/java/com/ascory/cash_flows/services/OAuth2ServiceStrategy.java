package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.OAuth2ServiceName;
import com.ascory.cash_flows.models.User;

public interface OAuth2ServiceStrategy {
    String getAccessTokenFromRegisterService(String code);
    String getAccessTokenFromAuthenticationService(String code);
    String getAccessTokenFromVerificationService(String code);
    String getOAuth2Id(String accessToken);
    User getUserByOAuth2Id(String oAuth2Id);
    boolean existsByOAuth2Id(String oAuth2Id);
    OAuth2ServiceName getOAuth2ServiceName();
    void setOAuth2IdToUser(User user, String oAuth2Id);
    boolean checkIsVerificationNotExists(User user);
    void deleteVerification(User user);
}
