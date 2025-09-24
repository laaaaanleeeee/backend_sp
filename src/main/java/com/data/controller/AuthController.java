package com.data.controller;

import com.data.dto.request.*;
import com.data.dto.response.PageDTO;
import com.data.dto.response.UserResponseDTO;
import com.data.dto.response.AuthResponse;
import com.data.entity.User;
import com.data.enums.UserRole;
import com.data.security.JwtUtil;
import com.data.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody SignUpRequestDTO req) {
        if (userService.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setDob(req.getDob());
        user.setUserGender(req.getUserGender());
        user.setCreatedAt(LocalDateTime.now());
        user.setUserRole(UserRole.CLIENT);
        User saved = userService.saveUser(user);
        return ResponseEntity.ok(new UserResponseDTO(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            User user = (User) auth.getPrincipal();
            String access = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().name());
            String refresh = jwtUtil.generateRefreshToken(user.getUsername());

            var resp = new AuthResponse(access, refresh, 3600000L, new UserResponseDTO(user));
            return ResponseEntity.ok(resp);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest req) {
        try {
            String refreshToken = req.getRefreshToken();
            String username = jwtUtil.getUsername(refreshToken);
            if (jwtUtil.isExpired(refreshToken)) {
                return ResponseEntity.status(401).body("Refresh token expired");
            }

            var user = (User) userService.loadUserByUsername(username);
            String newAccess = jwtUtil.generateAccessToken(user.getUsername(), user.getUserRole().name());
            var resp = new AuthResponse(newAccess, refreshToken, 3600000L, new UserResponseDTO(user));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setUserGender(request.getUserGender());

        User updated = userService.saveUser(user);

        return ResponseEntity.ok(new UserResponseDTO(updated));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<PageDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName,
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.getAllUsers(pageable, username, email, fullName);

        Page<UserResponseDTO> dtoPage = usersPage.map(UserResponseDTO::new);
        return ResponseEntity.ok(PageDTO.of(dtoPage));
    }

    @PostMapping("/admin/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody SignUpRequestDTO req) {
        if (userService.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setDob(req.getDob());
        user.setUserGender(req.getUserGender());
        user.setCreatedAt(LocalDateTime.now());
        user.setUserRole(req.getUserRole());

        User saved = userService.saveUser(user);
        return ResponseEntity.ok(new UserResponseDTO(saved));
    }

    @PutMapping("/admin/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserAdminRequest req
    ) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setDob(req.getDob());
        user.setUserGender(req.getUserGender());
        user.setUserRole(req.getUserRole());

        User updated = userService.saveUser(user);
        return ResponseEntity.ok(new UserResponseDTO(updated));
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
