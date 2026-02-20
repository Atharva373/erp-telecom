package com.atharva.erp_telecom.iam.service;


import com.atharva.erp_telecom.iam.dto.RegisterResponse;
import com.atharva.erp_telecom.iam.persistence.entity.Role;
import com.atharva.erp_telecom.iam.persistence.entity.User;
import com.atharva.erp_telecom.exception.custom_exceptions.InvalidCredentialsException;
import com.atharva.erp_telecom.exception.custom_exceptions.RoleNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.UserAlreadyExistsException;
import com.atharva.erp_telecom.iam.persistence.repository.RoleRepository;
import com.atharva.erp_telecom.iam.persistence.repository.UserRepository;
import com.atharva.erp_telecom.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;              // injected from the SecurityConfig class
    @Autowired
    private final AuthenticationManager authenticationManager;  // injected from the SecurityConfig class
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserAuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    // Method to register a new user.
    public RegisterResponse registerNewUser(User user, Set<String> roleNames) {
        // Check if user already exists
        if (userRepository.existsByUserName(user.getUserName())) {
            String errorMessage = "Username already exists: " + user.getUserName();
            throw new UserAlreadyExistsException(errorMessage);
        }
        // Encode password using BCryptPassword Encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default value for 'enabled'
        if (user.getEnabled() == null) user.setEnabled(true);
        RegisterResponse response = new RegisterResponse();
        Set<Role> roleSetToBeChecked =
                roleNames.stream()
                        .map(role ->
                             roleRepository.findByRoleName(role)
                                     .orElseThrow(() -> new RoleNotFoundException("Role not found:" + role))
                        ).collect(Collectors.toSet());
        user.setRoles(roleSetToBeChecked);
        User savedUser = userRepository.save(user);
        return new RegisterResponse("User with username:" + savedUser.getUserName() + " created successfully.");
    }

    // Authenticate existing user and return a JWT token
    public String authenticate(String username, String password) {
        // NOTE:
        /*
            This authentication manager is responsible for checking if the credentials match or not. It implicitly calls
            the wrapper UserService Service and findByUserName() method
         */
        System.out.println("Before authenticating...");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password, please verify.");
        }
        System.out.println("After authenticating...");
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtUtils.generateToken(userDetails);
        System.out.println("Token: "+ token);
        return token;
    }
}
