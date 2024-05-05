package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.responses.GetAllVerificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(username))
                .orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }

    public GetAllVerificationsResponse getAllVerifications(Authentication authentication){
        if (authentication == null) {
            throw new AccessDeniedException("User is unauthenticated or did not provide a JWT token.");
        }
        User user = userRepository.getUserById(Long.valueOf(authentication.getName()));
        return GetAllVerificationsResponse.builder()
                .discordId(user.getDiscordId())
                .email(user.getEmail())
                .build();
    }
}
