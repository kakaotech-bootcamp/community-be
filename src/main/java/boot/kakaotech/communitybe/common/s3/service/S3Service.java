package boot.kakaotech.communitybe.common.s3.service;

import boot.kakaotech.communitybe.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadUserProfile(User user, MultipartFile file);

}
