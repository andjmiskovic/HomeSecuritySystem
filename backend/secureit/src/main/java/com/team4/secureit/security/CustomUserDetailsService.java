package com.team4.secureit.security;

import com.team4.secureit.exception.UserNotFoundException;
import com.team4.secureit.model.User;
import com.team4.secureit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (user.isAccountNonLocked())
            throw new LockedException(user.getLockReason());

        return user;
    }

    public UserDetails loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
