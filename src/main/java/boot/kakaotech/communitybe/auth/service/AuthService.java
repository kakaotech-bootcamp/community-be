package boot.kakaotech.communitybe.auth.service;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import boot.kakaotech.communitybe.auth.dto.ValueDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    void signup(SignupDto signupDto, MultipartFile file);

    boolean checkEmail(ValueDto valueDto);

    boolean checkNickname(ValueDto valueDto);

}
