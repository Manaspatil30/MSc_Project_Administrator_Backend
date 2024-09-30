package com.app.msc_project_administrator.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/register/csv")
  public ResponseEntity<String> registerUsersFromCsv(@RequestParam("file") MultipartFile file) {
    try {
      List<RegisterRequest> users = service.parseCsv(file);
      users.forEach(registerRequest -> service.register(registerRequest));
      return ResponseEntity.ok("Users registered successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register users: " + e.getMessage());
    }
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    try {
      return ResponseEntity.ok(service.authenticate(request));
    } catch (RuntimeException ex){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(ex.getLocalizedMessage(), null));
    }
  }
}
