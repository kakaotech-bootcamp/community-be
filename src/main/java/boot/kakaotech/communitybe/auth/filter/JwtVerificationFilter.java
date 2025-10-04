package boot.kakaotech.communitybe.auth.filter;

import boot.kakaotech.communitybe.auth.service.CustomUserDetailsService;
import boot.kakaotech.communitybe.auth.service.JwtService;
import boot.kakaotech.communitybe.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private final PathPatternParser patternParser = new PathPatternParser();
    private final CookieUtil cookieUtil;

    private final List<PathPattern> excludedUrls = Arrays.asList(
            "/api/auth/**"
    ).stream().map(patternParser::parse).toList();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JwtVerificationFilter] 토큰 검증 시작");

        String authHeader = request.getHeader(AUTHORIZATION);
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        if (accessToken != null) {
            try {
                processAccessToken(accessToken, response);
            } catch (ExpiredJwtException e) {
                log.info("[JwtVerificationFilter] Access token 만료");

                // TODO: refresh token으로 갱신하는 로직 추가
                // 일단은 401로 처리
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        log.info("[JwtVerificationFilter] 토큰 검증 성공");
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        PathContainer container = PathContainer.parsePath(path);

        return excludedUrls.stream().anyMatch(p -> p.matches(container));
    }

    private void processAccessToken(String accessToken, HttpServletResponse response) {
        String email = jwtService.getEmailFromToken(accessToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =  userDetailsService.loadUserByUsername(email);

            if (jwtService.isValidAccessToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

}
