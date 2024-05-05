package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(username))
                .orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }
}
