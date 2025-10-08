package boot.kakaotech.communitybe.comment.controller;

import boot.kakaotech.communitybe.comment.dto.CommentDto;
import boot.kakaotech.communitybe.comment.dto.CreateCommentDto;
import boot.kakaotech.communitybe.comment.service.CommentService;
import boot.kakaotech.communitybe.common.scroll.dto.CursorPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<CursorPage<CommentDto>> getComments(
            @PathVariable("postId") Integer postId,
            @RequestParam("pid") Integer parentId,
            @RequestParam("cursor") Integer cursor,
            @RequestParam("size") Integer size
    ) {
        log.info("[CommentController] 댓글 조회 시작 - postId: {}, parentId: {}", postId, parentId);

        CursorPage<CommentDto> comments = commentService.getComments(postId, parentId, cursor, size);
        log.info("[CommentController] 댓글 조회 성공");

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<Integer> addComment(@PathVariable("postId") Integer postId, @RequestBody CreateCommentDto dto) {
        log.info("[CommentController] 댓글 생성 시작 - postId: {}", postId);

        Integer commentId = commentService.addComment(postId, dto);
        log.info("[CommentController] 댓글 생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

}
