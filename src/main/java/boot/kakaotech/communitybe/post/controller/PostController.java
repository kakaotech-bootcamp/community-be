package boot.kakaotech.communitybe.post.controller;

import boot.kakaotech.communitybe.common.scroll.dto.CursorPage;
import boot.kakaotech.communitybe.post.dto.PostListWrapper;
import boot.kakaotech.communitybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<CursorPage<PostListWrapper>> getPosts(@RequestParam Integer cursor, @RequestParam Integer size) {
        log.info("[PostController] 게시글목록 조회 시작");

        CursorPage<PostListWrapper> posts = postService.getPosts(cursor, size);
        log.info("[PostController] 게시글목록 조회 성공");

        return ResponseEntity.ok(posts);
    }

}
