package boot.kakaotech.communitybe.config;

import boot.kakaotech.communitybe.auth.filter.JwtVerificationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtVerificationFilter jwtVerificationFilter) throws Exception {
        log.info("[SecurityConfig] 보안 필터 구성 시작");
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> {
                    requests
                                    .requestMatchers(
                                            "/"
                                    ).permitAll()
                                    .anyRequest().authenticated();
                    log.info("[SecurityConfig] URL 인가 구성 완료");
                })
                .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> {
                    form
                            .loginProcessingUrl("/api/auth/signin")
                    // TODO
//                            .successHandler()
//                            .failureHandler();
                    ;
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/api/auth/logout")
                            .addLogoutHandler((request, response, authentication) -> {
                                response.setStatus(HttpServletResponse.SC_OK);
                            })
                            .permitAll();
                })
                .sessionManagement(session -> {
                    log.info("[SecurityConfig] 세션 관리 정책: STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        return http.build();
    }

}
