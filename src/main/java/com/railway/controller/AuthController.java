// package com.railway.controller;

// import com.railway.dto.AuthRequest;
// import com.railway.dto.AuthResponse;
// import com.railway.dto.RegisterRequest;
// import com.railway.model.User;
// import com.railway.security.JwtUtil;
// import com.railway.security.RailwayUserDetails;
// import com.railway.service.AuthService;
// import jakarta.validation.Valid;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/auth")
// @CrossOrigin
// public class AuthController {
//     private final AuthService authService;
//     private final AuthenticationManager authenticationManager;
//     private final JwtUtil jwtUtil;

//     public AuthController(AuthService authService,
//                           AuthenticationManager authenticationManager,
//                           JwtUtil jwtUtil) {
//         this.authService = authService;
//         this.authenticationManager = authenticationManager;
//         this.jwtUtil = jwtUtil;
//     }

//     @PostMapping("/register")
//     public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
//         User user = authService.register(request);
//         RailwayUserDetails userDetails = RailwayUserDetails.from(user);
//         String token = jwtUtil.generateToken(userDetails);
//         return ResponseEntity.status(HttpStatus.CREATED)
//                 .body(new AuthResponse(token, user.getUsername(), user.getRole()));
//     }

//     // @PostMapping("/login")
//     // public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
//     //     Authentication authentication = authenticationManager.authenticate(
//     //             new UsernamePasswordAuthenticationToken(request.username(), request.password()));
//     //     RailwayUserDetails userDetails = (RailwayUserDetails) authentication.getPrincipal();
//     //     String token = jwtUtil.generateToken(userDetails);
//     //     return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), userDetails.getRole()));
//     // }
//     @PostMapping("/login")
// public ResponseEntity<?> login(@RequestBody LoginRequest request) {

//     System.out.println("LOGIN USERNAME: " + request.getUsername());
//     System.out.println("LOGIN PASSWORD: " + request.getPassword());

//     User user = userRepository.findByUsername(request.getUsername());

//     System.out.println("DB USER: " + user);

//     boolean match = passwordEncoder.matches(
//         request.getPassword(),
//         user.getPassword()
//     );

//     System.out.println("PASSWORD MATCH RESULT: " + match);

// }
// }
//new controller for login, register and logout
package com.railway.controller;

import com.railway.dto.AuthRequest;
import com.railway.dto.AuthResponse;
import com.railway.dto.RegisterRequest;
import com.railway.model.User;
import com.railway.security.JwtUtil;
import com.railway.security.RailwayUserDetails;
import com.railway.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        RailwayUserDetails userDetails = RailwayUserDetails.from(user);

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getUsername(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {

        System.out.println("LOGIN ATTEMPT: " + request.username());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        RailwayUserDetails userDetails =
                (RailwayUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);

        System.out.println("LOGIN SUCCESS: " + userDetails.getUsername());

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        userDetails.getUsername(),
                        userDetails.getRole()
                )
        );
    }
}