package com.moviereview.service;

import com.moviereview.domain.review.ReviewLike;

public interface ReviewLikeService {
    ReviewLike likeReview(Long reviewId, Long userId);
    
    void unlikeReview(Long reviewId, Long userId);
    
    boolean hasUserLikedReview(Long reviewId, Long userId);
    
    long countReviewLikes(Long reviewId);
} 