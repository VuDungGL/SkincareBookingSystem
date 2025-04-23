package com.devices.app.config.jwt;

import com.devices.app.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.function.Supplier;

@EnableMethodSecurity // Cho phép dùng @PreAuthorize, @Secured, etc.
@Configuration
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter, UserService userService) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/login",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/assets/**",
                            "/login/**",
                            "/register",
                            "/register/**",
                            "/Uploads/**",
                            "/appointment/**",
                            "/booking/**"
                    ).permitAll()

                    .requestMatchers(
                            "/", "/home", "/about", "/service", "/appointment", "/contact",
                            "/gallery", "/index", "/price", "/team", "/testimonial","/chooseTherapist","/HistoryBooking","/infoUser"
                    ).access(notAdminAccess())

                    .requestMatchers("/admin/**", "/admin").hasAnyRole("ADMIN", "MASTER_ADMIN")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    String uri = request.getRequestURI();
                    if (uri.contains("/admin")) {
                        response.sendRedirect("/login");
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String uri = request.getRequestURI();
                    if (uri.contains("/admin")) {
                        response.sendRedirect("/home");
                    } else {
                        response.sendRedirect("/login");
                    }
                })
            )
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    private AuthorizationManager<RequestAuthorizationContext> notAdminAccess() {
        return (Supplier<Authentication> authentication, RequestAuthorizationContext context) -> {
            var auth = authentication.get();
            boolean isNotAdmin = auth.getAuthorities().stream()
                    .noneMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMIN") ||
                                    authority.getAuthority().equals("ROLE_MASTER_ADMIN"));
            return new AuthorizationDecision(isNotAdmin);
        };
    }
}
