package com.ascory.cash_flows.services;

import com.ascory.cash_flows.models.User;
import com.ascory.cash_flows.repositories.JwtRefreshTokenRepository;
import com.ascory.cash_flows.repositories.UserRepository;
import com.ascory.cash_flows.responses.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public ResponseEntity<?> getAllUsers(){
        List<User> users = userRepository.findAll();
        ArrayList<GetUserResponse> getUserResponses = new ArrayList<>();
        for(User user: users){
            GetUserResponse getUserResponse = GetUserResponse.builder()
                    .id(user.getId())
                    .build();
            getUserResponses.add(getUserResponse);
        }
        return ResponseEntity.ok(getUserResponses);
    }

    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("there is no user with such id"));
        jwtRefreshTokenRepository.deleteIfExistsByUser(user);
        userRepository.delete(user);
    }
}
