package com.app.msc_project_administrator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import static com.app.msc_project_administrator.user.Permission.*;
import static com.app.msc_project_administrator.user.Role.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/books").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name())
                                .requestMatchers("/api/v1/users/**").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name(), STUDENT.name())
                                .requestMatchers("/api/v1/projects").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name(), STUDENT.name())
                                .requestMatchers("/api/sessions/**").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name(), STUDENT.name())
                                .requestMatchers("/api/taster-sessions/**").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name(), STUDENT.name())
                                .requestMatchers("/api/v1/projects/create").hasAnyRole(ACADEMIC.name())
                                .requestMatchers("/api/v1/projects/assign").hasAnyRole(MOD_OWNER.name())
                                .requestMatchers("/api/student-choices/**").hasAnyRole(STUDENT.name(), MOD_OWNER.name())
                                .requestMatchers("/api/v1/programs/add").hasAnyRole(MOD_OWNER.name(), ACADEMIC.name())
                                .requestMatchers("/api/v1/programs").hasAnyRole(STUDENT.name(), MOD_OWNER.name(), ACADEMIC.name())
                                .requestMatchers("/api/v1/project-assessment/assignProjectForAssessment").hasAnyRole((MOD_OWNER.name()))
                                .requestMatchers("/api/v1/project-assessment/supervisors").hasAnyRole(ACADEMIC.name(), MOD_OWNER.name())
                                .requestMatchers("/api/v1/project-assessment/viewAssignedProjects").hasAnyRole(ACADEMIC.name(), MOD_OWNER.name())
//                                .requestMatchers("/api/v1/projects").hasAnyRole(SUPERVISOR.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
