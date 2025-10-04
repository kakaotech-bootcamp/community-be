package boot.kakaotech.communitybe.auth.service;

import boot.kakaotech.communitybe.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    boolean isValidAccessToken(String accessToken, UserDetails user);

    boolean isValidRefreshToken(String refreshToken, UserDetails user);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String getEmailFromToken(String token);

    long getRefreshTokenExpireTime();

}
