package com.moviereview.service;

import com.moviereview.domain.review.Review;
import com.moviereview.dto.review.ReviewCreateRequest;
import com.moviereview.dto.review.ReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    Review createReview(Long userId, ReviewCreateRequest request);
    Review updateReview(Long userId, Long reviewId, ReviewUpdateRequest request);
    void deleteReview(Long userId, Long reviewId);
    Review getReview(Long reviewId);
    Page<Review> getReviewsByMovie(Long movieId, Pageable pageable);
    Page<Review> getReviewsByUser(Long userId, Pageable pageable);
    Page<Review> getNonSpoilerReviewsByMovie(Long movieId, Pageable pageable);
    Page<Review> getReviewsByMovieAndRating(Long movieId, Integer minRating, Pageable pageable);
    Page<Review> getReviewsByMovieOrderByLikeCount(Long movieId, Pageable pageable);
    Page<Review> getReviewsByMovieOrderByCommentCount(Long movieId, Pageable pageable);
    Page<Review> getActiveReviewsByUserId(Long userId, Pageable pageable);
    void likeReview(Long userId, Long reviewId);
    void unlikeReview(Long userId, Long reviewId);
    boolean hasUserLikedReview(Long userId, Long reviewId);
    
    Page<Review> getMovieReviews(Long movieId, Pageable pageable);
    
    Page<Review> getUserReviews(Long userId, Pageable pageable);
    
    Page<Review> getTopLikedReviews(Long movieId, Pageable pageable);
    
    Page<Review> getTopCommentedReviews(Long movieId, Pageable pageable);
    
    List<Review> getActiveReviewsByUserId(Long userId);
    
    long countActiveReviewsByMovieId(Long movieId);
    
    Double calculateAverageRatingByMovieId(Long movieId);
    
    boolean hasUserReviewedMovie(Long movieId, Long userId);
} 