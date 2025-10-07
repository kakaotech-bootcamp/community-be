package boot.kakaotech.communitybe.post.mapper;

import boot.kakaotech.communitybe.post.dto.PostListDto;
import boot.kakaotech.communitybe.post.entity.Post;
import boot.kakaotech.communitybe.user.dto.SimpUserInfo;
import boot.kakaotech.communitybe.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public SimpUserInfo getSimpUserInfo(User user) {
        return SimpUserInfo.builder()
                .id(user.getId())
                .name(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public PostListDto getPostListWrapper(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .viewCount(post.getViewCount())
                .commentCount(post.getComments().size())
                .likeCount(post.getLikes().size())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
