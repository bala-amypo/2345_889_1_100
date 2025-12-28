package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // ✅ REGISTER API
    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // encode inside service
        user.setRole("USER"); // default role

        User savedUser = userService.registerUser(user);

        String token = jwtUtil.generateToken(
                null,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    // ✅ LOGIN API
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        var authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        User user = userService.findByEmail(request.getEmail());

        String token = jwtUtil.generateToken(
                authToken,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}