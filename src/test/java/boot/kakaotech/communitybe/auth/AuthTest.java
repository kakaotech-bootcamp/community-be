package boot.kakaotech.communitybe.auth;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import boot.kakaotech.communitybe.auth.dto.ValueDto;
import boot.kakaotech.communitybe.auth.service.AuthService;
import boot.kakaotech.communitybe.user.entity.User;
import boot.kakaotech.communitybe.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 테스트")
    public void signupTest(){
        SignupDto signupDto = SignupDto.builder()
                .email("test@test.com")
                .password("test")
                .nickname("testUser")
                .build();

        authService.signup(signupDto, null);

        User saved = userRepository.findByEmail(signupDto.getEmail())
                .orElseThrow(() -> new AssertionError("유저가 저장되지 않았습니다."));
    }

    @Test
    @DisplayName("이메일 중복체크: 존재 테스트")
    public void duplicateExistedEmailTest(){
        ValueDto valueDto = ValueDto.builder()
                .value("test@test.com")
                .build();

        boolean isExist = authService.checkEmail(valueDto);

        if (!isExist) {
            throw new AssertionError("해당 유저는 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("이메일 중복체크: 미존재 테스트")
    public void duplicateNonExistedEmailTest(){
        ValueDto valueDto = ValueDto.builder()
                .value("nouser@test.com")
                .build();

        boolean isExist = authService.checkEmail(valueDto);

        if (isExist) {
            throw new AssertionError("해당 유저는 존재하지 않습니다.");
        }
    }

}
