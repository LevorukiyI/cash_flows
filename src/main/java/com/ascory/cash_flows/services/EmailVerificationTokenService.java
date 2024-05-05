package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.EmailPassVerificationTokenEntity;
import com.ascory.cash_flows.repositories.EmailPassRegisterRequestRepository;
import com.ascory.cash_flows.repositories.EmailPassVerificationTokenRepository;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {
    private final EmailSender emailSender;
    private final EmailPassVerificationTokenRepository verificationTokenRepository;
    private final EmailPassRegisterRequestRepository registerRequestRepository;

    public EmailPassVerificationTokenEntity createToken(
            EmailPassRegisterRequestEntity registerRequestEntity,
            Long userId) {
        registerRequestRepository.save(registerRequestEntity);

        EmailPassVerificationTokenEntity emailPassVerificationToken =
                EmailPassVerificationTokenEntity.builder()
                        .email(registerRequestEntity.getEmail())
                        .token(generateTokenString())
                        .registerRequestEntity(registerRequestEntity)
                        .userId(userId)
                        .build();
        verificationTokenRepository.save(emailPassVerificationToken);
        return emailPassVerificationToken;
    }

    public EmailPassVerificationTokenEntity getEmailPassVerificationTokenEntity(String token) {
        //TODO сделать проверку на истекший строк годности токена
        return verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Token."));
    }

    public void deleteTokens(
            EmailPassVerificationTokenEntity verificationTokenEntity,
            EmailPassRegisterRequestEntity registerRequestEntity){
        verificationTokenRepository.delete(verificationTokenEntity);
        registerRequestRepository.delete(registerRequestEntity);
    }

    private String generateTokenString() {//TODO добавить защиту от совпавших UUID добавлением id последней записи или пользователя
        return UUID.randomUUID().toString();
    }

    public void sendVerificationEmail(String email, String token) {
        String subject = "Подтверждение электронной почты";
        //#TODO не забыть поменять ссылку
        String body = "Для подтверждения электронной почты перейдите по ссылке: http://127.0.0.1:5500/authenticationPages/emailVerifyTokenPage.html?token=" + token;
        emailSender.sendEmail(email, subject, body);
    }
}
