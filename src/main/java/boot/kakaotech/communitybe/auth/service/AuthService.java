package boot.kakaotech.communitybe.auth.service;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    void signup(SignupDto signupDto, MultipartFile file);

}
