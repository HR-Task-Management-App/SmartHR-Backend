package com.hrms.backend.services;

import com.hrms.backend.exceptions.ResourceNotFoundException;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username is email in this project
        return userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User with this email not found"));
    }
}
