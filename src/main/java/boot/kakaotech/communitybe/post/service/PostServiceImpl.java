package boot.kakaotech.communitybe.post.service;

import boot.kakaotech.communitybe.common.scroll.dto.CursorPage;
import boot.kakaotech.communitybe.post.dto.CreatePostDto;
import boot.kakaotech.communitybe.post.dto.PostDetailWrapper;
import boot.kakaotech.communitybe.post.dto.PostListWrapper;
import boot.kakaotech.communitybe.post.entity.Post;
import boot.kakaotech.communitybe.post.entity.PostImage;
import boot.kakaotech.communitybe.post.repository.PostRepository;
import boot.kakaotech.communitybe.user.entity.User;
import boot.kakaotech.communitybe.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserUtil userUtil;

    @Override
    public CursorPage<PostListWrapper> getPosts(int cursor, int size) {
        log.info("[PostService] 게시글 목록 조회 시작");

        Pageable pageable = PageRequest.of(cursor, size);
        List<PostListWrapper> posts = postRepository.getPostsUsingFetch(pageable);

        if (posts.isEmpty()) {
            // TODO: 커스텀 에러 던지기
        }

        boolean hasNextCursor = posts.size() > size;
        Integer nextCursor = hasNextCursor ? posts.getLast().getPost().getId() : null;

        if (hasNextCursor) {
            posts.removeLast();
        }

        return CursorPage.<PostListWrapper>builder()
                .list(posts)
                .hasNextCursor(hasNextCursor)
                .nextCursor(nextCursor)
                .build();
    }

    @Override
    public PostDetailWrapper getPost(int postId) {
        log.info("[PostService] 게시글 상세조회 시작");

        PostDetailWrapper post = postRepository.getPostById(postId);

        if (post == null) {
            // TODO: 커스텀 에러 던지기
        }

        // TODO: 레디스에서 viewCount 증가로직 추가

        return post;
    }

    @Override
    public Integer savePost(CreatePostDto createPostDto, List<String> images) {
        log.info("[PostService] 게시글 생성 시작");

        User author = userUtil.getCurrentUser();

        Post post = Post.builder()
                .author(author)
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .viewCount(0)
                .build();

        List<PostImage> imagesList = null;
        // TODO: presigned url 발급받는 로직 추가

        postRepository.save(post);
        return post.getId();
    }

}
