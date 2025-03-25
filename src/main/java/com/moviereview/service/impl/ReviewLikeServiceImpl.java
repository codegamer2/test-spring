package com.moviereview.service.impl;

import com.moviereview.domain.review.Review;
import com.moviereview.domain.review.ReviewLike;
import com.moviereview.domain.user.User;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.repository.ReviewLikeRepository;
import com.moviereview.service.ReviewLikeService;
import com.moviereview.service.ReviewService;
import com.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeServiceImpl implements ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    @Override
    @Transactional
    public ReviewLike likeReview(Long reviewId, Long userId) {
        Review review = reviewService.getReview(reviewId);
        User user = userService.getUser(userId);

        if (hasUserLikedReview(reviewId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 리뷰입니다.");
        }

        ReviewLike reviewLike = new ReviewLike(review, user);
        review.incrementLikeCount();
        return reviewLikeRepository.save(reviewLike);
    }

    @Override
    @Transactional
    public void unlikeReview(Long reviewId, Long userId) {
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndUserIdAndIsActiveTrue(reviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("좋아요를 찾을 수 없습니다."));

        Review review = reviewLike.getReview();
        review.decrementLikeCount();
        
        reviewLike.deactivate();
        reviewLikeRepository.save(reviewLike);
    }

    @Override
    public boolean hasUserLikedReview(Long reviewId, Long userId) {
        return reviewLikeRepository.existsByReviewIdAndUserIdAndIsActiveTrue(reviewId, userId);
    }

    @Override
    public long countReviewLikes(Long reviewId) {
        return reviewLikeRepository.countActiveLikesByReviewId(reviewId);
    }
} 