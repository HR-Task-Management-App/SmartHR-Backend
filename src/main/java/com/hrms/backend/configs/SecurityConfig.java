package com.hrms.backend.configs;

import com.hrms.backend.security.JwtAuthenticationEntryPoint;
import com.hrms.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    };

    private final AccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(AccessDeniedHandler customAccessDeniedHandler) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // not required in jwt token base auth.
        httpSecurity.cors(Customizer.withDefaults());

        //the order of requestMatchers() matters.
        // Authorization rules are evaluated in the sequence they are declared, and the first matching rule is applied.
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(PUBLIC_URLS).permitAll() // later do for user controller only for hr {companyCode part}
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers("/chat-websocket","/chat-websocket/**", "/webrtc/**").permitAll()
                        .requestMatchers("/ping").permitAll()
                        .requestMatchers("/chat","/chat/**").permitAll()
                        .requestMatchers("/chats","/chats/**").hasAnyAuthority("ROLE_HR","ROLE_ADMIN","ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/profile-image").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/leave-company").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/remove-wait-company").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tasks").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tasks/*").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tasks/companyTasks").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tasks/userTasks").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tasks/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tasks/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tasks/status/*").hasAnyAuthority("ROLE_USER", "ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/offices").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/offices/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/offices").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/meetings/create").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/meetings/myMeetings").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/meetings/respond/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/meetings/cancel/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/meetings/*").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/meetings/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/leaves").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/leaves").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/leaves/company").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/leaves/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/leaves/status/*/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/leaves/response/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/companies/empWaitlist").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/companies/employees").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/companies/acceptEmployee/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/companies/rejectEmployee/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/companies/removeEmployee/*").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/companies/everybody").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/comments").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/comments/*").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/chats/history").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/chats/myChats").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/chats/seen/*").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/attendance").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/attendance/history").hasAnyAuthority("ROLE_HR", "ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/attendance/company").hasAnyAuthority("ROLE_HR", "ROLE_ADMIN")
                        .anyRequest().authenticated()
        ).exceptionHandling(ex -> ex
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //for checking if token is authorized or not
        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();

    }
}
