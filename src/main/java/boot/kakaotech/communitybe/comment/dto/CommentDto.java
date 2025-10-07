package boot.kakaotech.communitybe.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;

    private Integer userId;

    private String name;

    private String profileImageUrl;

    private String comment;

    private Integer parentId;

    private int depth;

    private LocalDateTime createdAt;

}
