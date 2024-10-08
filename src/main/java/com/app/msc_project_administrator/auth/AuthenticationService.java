package com.app.msc_project_administrator.auth;




import com.app.msc_project_administrator.config.JwtService;
//import com.app.msc_project_administrator.token.Token;
//import com.app.msc_project_administrator.token.TokenRepository;
//import com.app.msc_project_administrator.token.TokenType;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserDTO;
import com.app.msc_project_administrator.user.UserRepository;
import com.app.msc_project_administrator.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }

  public List<RegisterRequest> parseCsv(MultipartFile file) throws Exception {
    try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
      CsvToBean<RegisterRequest> csvToBean = new CsvToBeanBuilder<RegisterRequest>(reader)
              .withType(RegisterRequest.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build();

      return csvToBean.parse();
    }
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("The email you entered is not registered."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
      throw new RuntimeException("The password you entered is incorrect.");
    }

    String jwtToken = jwtService.generateToken(user);
    UserDTO userDTO = userService.mapToUserDTO(user);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .user(userDTO)
            .build();
  }
}
