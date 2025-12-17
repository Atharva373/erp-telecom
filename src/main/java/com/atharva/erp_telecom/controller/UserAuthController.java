package com.atharva.erp_telecom.controller;


import com.atharva.erp_telecom.dto.AuthRequest;
import com.atharva.erp_telecom.dto.AuthResponse;
import com.atharva.erp_telecom.dto.RegisterRequest;
import com.atharva.erp_telecom.dto.RegisterResponse;
import com.atharva.erp_telecom.entity.User;
import com.atharva.erp_telecom.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    // Endpoint to create a new user
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        User userToSend = new User();
        userToSend.setUserName(request.getUserName());
        userToSend.setPassword(request.getPassword());
        userToSend.setUserFirstName(request.getUserFirstName());
        userToSend.setUserLastName(request.getUserLastName());

        RegisterResponse response = userAuthService.registerNewUser(userToSend,request.getRoles());

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    // Endpoint to authenticate an exiting user and return a JWT token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        System.out.println("Inside the login controller...\nUsername: "+authRequest.getUsername()+"\tPassword: "+authRequest.getPassword());
        String jwtToken = userAuthService.authenticate(authRequest.getUsername(),authRequest.getPassword());
        System.out.println("Token returned to the controller...\nToken: "+jwtToken);
        return new ResponseEntity<>(new AuthResponse(jwtToken),HttpStatus.OK);
    }
}
