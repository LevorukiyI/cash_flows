package com.ascory.cash_flows.services;

import com.ascory.cash_flows.exceptions.UserNotFoundException;
import com.ascory.cash_flows.models.EmailPassVerificationTokenEntity;
import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.requests.EmailPassAuthenticateRequest;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class EmailPassAuthService{

    private final UserRepository userRepository;
    private final AuthenticationHandler authenticationHandler;
    private final AuthenticationManager authenticationManager;
    private final EmailPassValidator emailPassValidator;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(
            EmailPassAuthenticateRequest emailPassAuthenticateRequest){
        User user = userRepository.findByEmail(emailPassAuthenticateRequest.getEmail())
                .orElseThrow(()-> new UserNotFoundException("User with such email not exists"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        emailPassAuthenticateRequest.getPassword()
                )
        );
        if(!user.getUsername().equals((String) authentication.getPrincipal())){
            throw new BadCredentialsException("Bad credentials, password doesn't matches");
        }
        return authenticationHandler.authenticateAndGetAuthenticationResponse(user);
    }

    public void createEmailVerificationToken(
            EmailPassRegisterRequestEntity emailPassRegisterRequestEntity,
            Authentication authentication) {//add principal as null, if you're creating new user
        emailPassValidator.validateEmailPassRegisterRequest(emailPassRegisterRequestEntity);
        Long userId = null;
        if(authentication!=null){
            userId = Long.valueOf(authentication.getName());
        }
        EmailPassVerificationTokenEntity emailVerificationToken =
                emailVerificationTokenService.createToken(emailPassRegisterRequestEntity, userId);
        emailVerificationTokenService.sendVerificationEmail(
                emailPassRegisterRequestEntity.getEmail(), emailVerificationToken.getToken());
    }

    public AuthenticationResponse verifyEmailPassToken(String token){
        EmailPassVerificationTokenEntity verificationTokenEntity =
                emailVerificationTokenService.getEmailPassVerificationTokenEntity(token);
        EmailPassRegisterRequestEntity registerRequestEntity = verificationTokenEntity.getRegisterRequestEntity();
        User user;
        if(verificationTokenEntity.getUserId() == null){
            user = this.registerUserByToken(registerRequestEntity);
        }
        else{
            user = this.confirmEmailVerificationByToken(verificationTokenEntity, registerRequestEntity);
        }

        emailVerificationTokenService.deleteTokens(verificationTokenEntity, registerRequestEntity);

        return authenticationHandler.authenticateAndGetAuthenticationResponse(user);
    }

    public User confirmEmailVerificationByToken(
            EmailPassVerificationTokenEntity verificationTokenEntity,
            EmailPassRegisterRequestEntity registerRequestEntity){

        User user = userRepository.getUserById(verificationTokenEntity.getUserId());
        this.setEmailPassToUser(registerRequestEntity, user);
        userRepository.save(user);
        return user;
    }

    public User registerUserByToken(
            EmailPassRegisterRequestEntity registerRequestEntity){

        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createUser(registerRequestEntity);
        userRepository.save(user);
        return user;
    }

    public void setEmailPassToUser(EmailPassRegisterRequestEntity registerRequestEntity, User user){
        user.setEmail(registerRequestEntity.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestEntity.getPassword()));//#TODO шифровать пароль перед установкой
    }
}
