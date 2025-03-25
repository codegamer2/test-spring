package com.moviereview.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserIdAndIsActiveTrue(Long commentId, Long userId);
    boolean existsByCommentIdAndUserIdAndIsActiveTrue(Long commentId, Long userId);

    @Query("SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = :commentId AND cl.isActive = true")
    long countActiveLikesByCommentId(@Param("commentId") Long commentId);
} 