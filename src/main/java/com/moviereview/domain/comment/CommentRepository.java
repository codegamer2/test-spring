package com.moviereview.domain.comment;

import com.moviereview.domain.review.Review;
import com.moviereview.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByReview(Review review, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
    List<Comment> findByParent(Comment parent);

    @Query("SELECT c FROM Comment c WHERE c.review = :review AND c.parent IS NULL")
    Page<Comment> findRootCommentsByReview(@Param("review") Review review, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.review = :review ORDER BY c.likeCount DESC")
    Page<Comment> findByReviewOrderByLikeCountDesc(@Param("review") Review review, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.review = :review AND c.parent IS NULL ORDER BY c.createdAt DESC")
    Page<Comment> findRootCommentsByReviewOrderByCreatedAtDesc(@Param("review") Review review, Pageable pageable);
} 