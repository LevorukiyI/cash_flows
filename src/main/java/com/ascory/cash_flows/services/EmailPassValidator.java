package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.RegistrationData;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationValidator {

    private final UserRepository userRepository;

    public boolean validateRegistrationData(RegistrationData registrationData){
        return true;//#TODO сделать нормально
    }

    public void validateEmailPassRegisterRequest(EmailPassRegisterRequestEntity emailPassRegisterRequestEntity){
        if (!emailPassRegisterRequestEntity.getPassword().equals(emailPassRegisterRequestEntity.getConfirmPassword())){
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }
        if(userRepository.existsByEmail(emailPassRegisterRequestEntity.getEmail())){
            throw new IllegalArgumentException("user with such email already exists");
        }
        //#TODO сделать нормально
    }
}
