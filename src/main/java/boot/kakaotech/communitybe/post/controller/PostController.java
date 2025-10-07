package boot.kakaotech.communitybe.post.controller;

import boot.kakaotech.communitybe.common.scroll.dto.CursorPage;
import boot.kakaotech.communitybe.post.dto.CreatePostDto;
import boot.kakaotech.communitybe.post.dto.PostDetailWrapper;
import boot.kakaotech.communitybe.post.dto.PostListWrapper;
import boot.kakaotech.communitybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailWrapper> getPost(@PathVariable("postId") Integer postId) {
        log.info("[PostController] 게시글 상세조회 시작");

        PostDetailWrapper post = postService.getPost(postId);
        log.info("[PostController] 게시글 상세조회 성공");

        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Integer> savePost(@RequestBody CreatePostDto createPostDto, @RequestBody List<String> images) {
        log.info("[PostController] 게시글 생성 시작");

        Integer postId = postService.savePost(createPostDto, images);
        log.info("[PostController] 게시글 생성 성공");

        return ResponseEntity.ok(postId);
    }

}
