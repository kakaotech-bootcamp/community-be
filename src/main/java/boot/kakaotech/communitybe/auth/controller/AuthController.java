package boot.kakaotech.communitybe.auth.controller;

import boot.kakaotech.communitybe.auth.dto.SignupDto;
import boot.kakaotech.communitybe.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestPart("data") SignupDto signupDto,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        log.info("[AuthController] 회원가입 요청 시작");

        authService.signup(signupDto, file);
        return ResponseEntity.ok().build();
    }

}
