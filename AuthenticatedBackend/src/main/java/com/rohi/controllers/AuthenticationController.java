package com.rohi.controllers;

import com.rohi.models.*;
import com.rohi.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohi.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RefreshTokenService refreshTokenService;



    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }
    
    @PostMapping("/login")
    public JwtResponseDTO loginUser(@RequestBody RegistrationDTO body){
        String username = body.getUsername();
        String password = body.getPassword();

        // Perform authentication logic here
        // ...
        // Assuming authentication is successful, generate the access token and refresh token

        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(username, password);
        String accessToken = loginResponseDTO.getJwt();

        // Generate and save the refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
        String refreshTokenString = refreshToken.getToken();

        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .build();
    }


    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody  RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getApplicationUser)
                .map(ApplicationUser ->{
                    LoginResponseDTO loginResponseDTO = authenticationService.loginUser(ApplicationUser.getUsername(), ApplicationUser.getPassword());
                    String accessToken = loginResponseDTO.getJwt();
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequest.token())
                            .build();
                }).orElseThrow(()-> new RuntimeException(("Refresh Token is not found in database")));

    }
}   
