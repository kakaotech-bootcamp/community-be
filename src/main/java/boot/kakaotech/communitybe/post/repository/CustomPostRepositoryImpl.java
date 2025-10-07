package boot.kakaotech.communitybe.post.repository;

import boot.kakaotech.communitybe.comment.dto.CommentDto;
import boot.kakaotech.communitybe.post.dto.PostDetailDto;
import boot.kakaotech.communitybe.post.dto.PostDetailWrapper;
import boot.kakaotech.communitybe.post.dto.PostListDto;
import boot.kakaotech.communitybe.post.dto.PostListWrapper;
import boot.kakaotech.communitybe.user.dto.SimpUserInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static boot.kakaotech.communitybe.comment.entity.QComment.comment;
import static boot.kakaotech.communitybe.post.entity.QPost.post;
import static boot.kakaotech.communitybe.post.entity.QPostImage.postImage;
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
                .leftJoin(post.likes, postLike)
                .leftJoin(post.comments, comment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return posts;
    }

    @Override
    public PostDetailWrapper getPostById(int postId) {
        PostDetailWrapper postDetail = jpaQueryFactory
                .select(
                        Projections.constructor(PostDetailWrapper.class,
                                Projections.constructor(SimpUserInfo.class,
                                                user.id,
                                                user.nickname.as("name"),
                                                user.profileImageUrl)
                                        .as("author"),
                                Projections.constructor(PostDetailDto.class,
                                                post.id,
                                                post.title,
                                                Projections.list(postImage.url),
                                                post.content,
                                                postLike.count().as("likeCount"),
                                                comment.count().as("commentCount"),
                                                post.viewCount,
                                                post.createdAt)
                                        .as("post")
                        )
                )
                .from(post)
                .join(post.author, user)
                .leftJoin(post.likes, postLike)
                .leftJoin(post.comments, comment)
                .leftJoin(post.images, postImage)
                .where(post.id.eq(postId))
                .fetchOne();

        return postDetail;
    }

}
