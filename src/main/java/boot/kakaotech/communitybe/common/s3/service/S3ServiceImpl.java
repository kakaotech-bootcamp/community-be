package boot.kakaotech.communitybe.common.s3.service;

import boot.kakaotech.communitybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {


    @Override
    public String uploadUserProfile(User user, MultipartFile file) {
        // TODO: S3에 이미지 업로드 후 저장된 url 반환 로직 구현
        return "";
    }
}
