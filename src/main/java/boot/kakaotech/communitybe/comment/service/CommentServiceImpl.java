package boot.kakaotech.communitybe.comment.service;

import boot.kakaotech.communitybe.comment.dto.CommentDto;
import boot.kakaotech.communitybe.comment.repository.CommentRepository;
import boot.kakaotech.communitybe.common.scroll.dto.CursorPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CursorPage<CommentDto> getComments(Integer postId, Integer parentId, Integer cursor, Integer size) {
        log.info("[CommentService] 댓글 조회 시작");

        Pageable pageable = PageRequest.of(cursor, size);
        List<CommentDto> comments = commentRepository.getComments(postId, parentId, pageable);

        if (comments.isEmpty()) {
            // TODO: 커스텀 에러 던지기
        }

        boolean hasNextCursor = comments.size() > size;
        Integer nextCursor = hasNextCursor ? comments.getLast().getId() : null;

        return CursorPage.<CommentDto>builder()
                .list(comments)
                .hasNextCursor(hasNextCursor)
                .nextCursor(nextCursor)
                .build();
    }

}
