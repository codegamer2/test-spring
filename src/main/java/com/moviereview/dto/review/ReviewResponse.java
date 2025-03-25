package com.moviereview.dto.review;

import com.moviereview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private Integer rating;
    private boolean hasSpoiler;
    private long likeCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setMovieId(review.getMovie().getId());
        response.setMovieTitle(review.getMovie().getTitle());
        response.setUserId(review.getUser().getId());
        response.setUsername(review.getUser().getUsername());
        response.setTitle(review.getTitle());
        response.setContent(review.getContent());
        response.setRating(review.getRating());
        response.setHasSpoiler(review.isHasSpoiler());
        response.setLikeCount(review.getLikeCount());
        response.setCommentCount(review.getCommentCount());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
} 