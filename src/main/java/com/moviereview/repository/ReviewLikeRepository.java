package com.moviereview.repository;

import com.moviereview.domain.review.ReviewLike;
import com.moviereview.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewIdAndUserIdAndIsActiveTrue(Long reviewId, Long userId);
    
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId AND rl.isActive = true")
    long countActiveLikesByReviewId(@Param("reviewId") Long reviewId);
    
    boolean existsByReviewIdAndUserIdAndIsActiveTrue(Long reviewId, Long userId);
} 