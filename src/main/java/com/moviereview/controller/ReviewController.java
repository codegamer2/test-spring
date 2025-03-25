package com.moviereview.controller;

import com.moviereview.domain.review.Review;
import com.moviereview.dto.review.ReviewCreateRequest;
import com.moviereview.dto.review.ReviewResponse;
import com.moviereview.dto.review.ReviewUpdateRequest;
import com.moviereview.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ReviewCreateRequest request) {
        Review review = reviewService.createReview(userId, request);
        return ReviewResponse.from(review);
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @PutMapping("/{id}")
    public ReviewResponse updateReview(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "리뷰 ID") @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest request) {
        Review review = reviewService.updateReview(userId, id, request);
        return ReviewResponse.from(review);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "리뷰 ID") @PathVariable Long id) {
        reviewService.deleteReview(userId, id);
    }

    @Operation(summary = "리뷰 조회", description = "리뷰 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ReviewResponse getReview(@Parameter(description = "리뷰 ID") @PathVariable Long id) {
        Review review = reviewService.getReview(id);
        return ReviewResponse.from(review);
    }

    @Operation(summary = "영화별 리뷰 조회", description = "특정 영화의 리뷰 목록을 조회합니다.")
    @GetMapping("/movie/{movieId}")
    public Page<ReviewResponse> getReviewsByMovie(
            @Parameter(description = "영화 ID") @PathVariable Long movieId,
            Pageable pageable) {
        return reviewService.getReviewsByMovie(movieId, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "사용자별 리뷰 조회", description = "특정 사용자의 리뷰 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public Page<ReviewResponse> getReviewsByUser(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            Pageable pageable) {
        return reviewService.getReviewsByUser(userId, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "스포일러 없는 리뷰 조회", description = "스포일러가 없는 리뷰만 조회합니다.")
    @GetMapping("/movie/{movieId}/non-spoiler")
    public Page<ReviewResponse> getNonSpoilerReviewsByMovie(
            @Parameter(description = "영화 ID") @PathVariable Long movieId,
            Pageable pageable) {
        return reviewService.getNonSpoilerReviewsByMovie(movieId, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "평점 기준 리뷰 조회", description = "최소 평점 이상의 리뷰를 조회합니다.")
    @GetMapping("/movie/{movieId}/rating")
    public Page<ReviewResponse> getReviewsByMovieAndRating(
            @Parameter(description = "영화 ID") @PathVariable Long movieId,
            @Parameter(description = "최소 평점") @RequestParam Integer minRating,
            Pageable pageable) {
        return reviewService.getReviewsByMovieAndRating(movieId, minRating, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "좋아요 순 리뷰 조회", description = "좋아요가 많은 순으로 리뷰를 조회합니다.")
    @GetMapping("/movie/{movieId}/likes")
    public Page<ReviewResponse> getReviewsByMovieOrderByLikeCount(
            @Parameter(description = "영화 ID") @PathVariable Long movieId,
            Pageable pageable) {
        return reviewService.getReviewsByMovieOrderByLikeCount(movieId, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "댓글 순 리뷰 조회", description = "댓글이 많은 순으로 리뷰를 조회합니다.")
    @GetMapping("/movie/{movieId}/comments")
    public Page<ReviewResponse> getReviewsByMovieOrderByCommentCount(
            @Parameter(description = "영화 ID") @PathVariable Long movieId,
            Pageable pageable) {
        return reviewService.getReviewsByMovieOrderByCommentCount(movieId, pageable)
                .map(ReviewResponse::from);
    }

    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아요를 표시합니다.")
    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeReview(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "리뷰 ID") @PathVariable Long id) {
        reviewService.likeReview(userId, id);
    }

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰의 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeReview(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "리뷰 ID") @PathVariable Long id) {
        reviewService.unlikeReview(userId, id);
    }
} 