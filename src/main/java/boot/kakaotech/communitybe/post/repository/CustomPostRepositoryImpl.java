package boot.kakaotech.communitybe.post.repository;

import boot.kakaotech.communitybe.post.dto.PostListDto;
import boot.kakaotech.communitybe.post.dto.PostListWrapper;
import boot.kakaotech.communitybe.user.dto.SimpUserInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static boot.kakaotech.communitybe.comment.entity.QComment.comment;
import static boot.kakaotech.communitybe.post.entity.QPost.post;
import static boot.kakaotech.communitybe.post.entity.QPostLike.postLike;
import static boot.kakaotech.communitybe.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostListWrapper> getPostsUsingFetch(Pageable pageable) {
        List<PostListWrapper> posts = jpaQueryFactory
                .select(Projections.constructor(PostListWrapper.class,
                        Projections.constructor(PostListDto.class,
                                post.id,
                                post.title,
                                postLike.count().as("likeCount"),
                                comment.count().as("commentCount"),
                                post.viewCount,
                                post.createdAt)
                                .as("post"),
                        Projections.constructor(SimpUserInfo.class,
                                user.id,
                                user.nickname.as("name"),
                                user.profileImageUrl)
                                .as("author")
                        ))
                .from(post)
                .join(post.author, user)
                .leftJoin(post.likes, postLike).fetchJoin()
                .leftJoin(post.comments, comment).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return posts;
    }

}
