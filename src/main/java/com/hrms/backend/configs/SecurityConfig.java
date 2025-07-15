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
                request.requestMatchers(PUBLIC_URLS).permitAll() // later do for user controller only for hr {companyCode part}
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers("/users/leave-company","/users/remove-wait-company").hasAuthority("ROLE_USER")
                        .requestMatchers("/users","/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/tasks","/tasks/**").hasAuthority("ROLE_HR")
                        .requestMatchers(HttpMethod.PUT,"/tasks/status").hasAnyAuthority("ROLE_HR","ROLE_USER")
                        .requestMatchers(HttpMethod.PUT,"/tasks","/tasks/**").hasAuthority("ROLE_HR")
                        .requestMatchers(HttpMethod.DELETE,"/tasks","/tasks/**").hasAuthority("ROLE_HR")
                        .requestMatchers("/tasks","/tasks/**").hasAnyAuthority("ROLE_HR","ROLE_USER")
                        .requestMatchers("/companies/**").hasAuthority("ROLE_HR")
                        .requestMatchers("/comments","/comments/**").hasAnyAuthority("ROLE_HR","ROLE_USER")
                        .anyRequest().hasAuthority("ROLE_ADMIN")
//                        .anyRequest().permitAll()
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
