package boot.kakaotech.communitybe.auth.controller;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import boot.kakaotech.communitybe.auth.dto.ValueDto;
import boot.kakaotech.communitybe.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestPart("data") SignupDto signupDto,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        log.info("[AuthController] 회원가입 요청 시작");

        authService.signup(signupDto, file);
        log.info("[AuthController] 회원가입 성공");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/duplications/email")
    public ResponseEntity<Void> duplicateEmail(
            @RequestBody ValueDto email
            ) {
        log.info("[AuthController] 이메일 중복확인 시작");

        boolean isExist = authService.checkEmail(email);
        log.info("[AuthController] 이메일 중복확인 성공");

        return isExist ?
                ResponseEntity.status(HttpStatus.CONFLICT).build() :
                ResponseEntity.noContent().build();
    }

    @PostMapping("/duplications/nickname")
    public ResponseEntity<Void> duplicateNickname(
            @RequestBody ValueDto nickname
    ) {
        log.info("[AuthController] 닉네임 중복확인 시작");

        boolean isExist = authService.checkNickname(nickname);
        log.info("[AuthController] 닉네임 중복확인 성공");

        return isExist ?
                ResponseEntity.status(HttpStatus.CONFLICT).build() :
                ResponseEntity.noContent().build();
    }

}
