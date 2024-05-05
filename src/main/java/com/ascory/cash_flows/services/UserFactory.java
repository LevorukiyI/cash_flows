package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.OAuth2ServiceName;
import com.ascory.cash_flows.models.RegistrationData;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserFactory {
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegistrationData registrationData, String oAuth2Id, OAuth2ServiceName OAuth2ServiceName) {
        switch (OAuth2ServiceName){
            case DISCORD:
                return User.builder()
                        .discordId(oAuth2Id)
                        .build();
        }
        return null;//#TODO сделать норм фабрику
    }

    public User createUser(
            EmailPassRegisterRequestEntity emailPassRegisterRequestEntity
                           ){
        return User.builder()
                .email(emailPassRegisterRequestEntity.getEmail())
                .password(passwordEncoder.encode(emailPassRegisterRequestEntity.getPassword()))
                .build();
    }//#TODO сделать норм фабрику
}
