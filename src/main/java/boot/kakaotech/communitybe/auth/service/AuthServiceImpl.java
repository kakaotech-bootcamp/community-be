package boot.kakaotech.communitybe.auth.service;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import boot.kakaotech.communitybe.common.s3.service.S3Service;
import boot.kakaotech.communitybe.user.entity.User;
import boot.kakaotech.communitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(SignupDto signupDto, MultipartFile file) {
        log.info("[AuthService] 회원가입 시작");

        validateSignup(signupDto);
        User user = User.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .build();

        userRepository.save(user);
        // TODO: s3Service의 uploadUserProfile을 비동기처리 후 update 쿼리
        String profileImageUrl = s3Service.uploadUserProfile(user, file);
        log.info("[AuthService] 회원가입 성공");
    }

    /**
     * signup 요청 시 받은 데이터 검증 메서드
     * 1. 이메일 형식 체크
     * 2. 비밀번호 길이, 영문 대문자, 소문자, 특수문자, 숫자 체크
     * 3. 닉네임 길이, 공백 체크
     *
     * @param signupDto
     */
    private void validateSignup(SignupDto signupDto) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=`~\\[\\]{};':\",./<>?]).+$";
        String nicknameRegex = ".*\\s.*";

        String email = signupDto.getEmail();
        if (email.matches(emailRegex)) {
            // TODO: 커스텀 에러 던지기
        }

        String password = signupDto.getPassword();
        if (password.length() < 8 || password.length() > 20 || password.matches(passwordRegex)) {
            // TODO: 커스텀 에러 던지기
        }

        String nickname = signupDto.getNickname();
        if (nickname.isEmpty() || nickname.length() > 10 || nickname.matches(nicknameRegex)) {
            // TODO: 커스텀 에러 던지기
        }
    }

}
