package com.hrms.backend.services;

import com.hrms.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrId) throws UsernameNotFoundException {
        // support both email (for login) and userId (for token validation)
        return userRepository.findByEmail(emailOrId)
                .or(() -> userRepository.findById(emailOrId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
