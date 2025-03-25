package com.moviereview.service.impl;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.review.Review;
import com.moviereview.domain.user.User;
import com.moviereview.dto.review.ReviewCreateRequest;
import com.moviereview.dto.review.ReviewUpdateRequest;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.repository.ReviewRepository;
import com.moviereview.service.MovieService;
import com.moviereview.service.ReviewService;
import com.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private final UserService userService;

    @Override
    @Transactional
    public Review createReview(Long userId, ReviewCreateRequest request) {
        User user = userService.getUser(userId);
        Movie movie = movieService.getMovie(request.getMovieId());

        if (reviewRepository.existsByMovieAndUser(movie, user)) {
            throw new IllegalStateException("이미 해당 영화에 대한 리뷰를 작성했습니다.");
        }

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .rating(request.getRating())
                .hasSpoiler(request.isHasSpoiler())
                .build();

        Review savedReview = reviewRepository.save(review);
        movie.incrementReviewCount();
        movieService.updateMovieRating(movie.getId());
        return savedReview;
    }

    @Override
    @Transactional
    public Review updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {
        Review review = getReview(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰를 수정할 권한이 없습니다.");
        }

        review.updateReview(request.getTitle(), request.getContent(), request.getRating(), request.isHasSpoiler(), null);
        Review updatedReview = reviewRepository.save(review);
        movieService.updateMovieRating(review.getMovie().getId());
        return updatedReview;
    }

    @Override
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = getReview(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰를 삭제할 권한이 없습니다.");
        }

        reviewRepository.delete(review);
        review.getMovie().decrementReviewCount();
        movieService.updateMovieRating(review.getMovie().getId());
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
    }

    @Override
    public Page<Review> getReviewsByMovie(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovie(movie, pageable);
    }

    @Override
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        return reviewRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Review> getNonSpoilerReviewsByMovie(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findNonSpoilerReviewsByMovie(movie, pageable);
    }

    @Override
    public Page<Review> getReviewsByMovieAndRating(Long movieId, Integer minRating, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovieAndRatingGreaterThanEqual(movie, minRating, pageable);
    }

    @Override
    public Page<Review> getReviewsByMovieOrderByLikeCount(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovieOrderByLikeCountDesc(movie, pageable);
    }

    @Override
    public Page<Review> getReviewsByMovieOrderByCommentCount(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovieOrderByCommentCountDesc(movie, pageable);
    }

    @Override
    @Transactional
    public void likeReview(Long userId, Long reviewId) {
        Review review = getReview(reviewId);
        review.incrementLikeCount();
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        Review review = getReview(reviewId);
        review.decrementLikeCount();
        reviewRepository.save(review);
    }

    @Override
    public boolean hasUserLikedReview(Long userId, Long reviewId) {
        return reviewRepository.existsByUserIdAndId(userId, reviewId);
    }

    @Override
    public Double calculateAverageRatingByMovieId(Long movieId) {
        return reviewRepository.calculateAverageRatingByMovieId(movieId);
    }

    @Override
    public Page<Review> getTopCommentedReviews(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovieOrderByCommentCountDesc(movie, pageable);
    }

    @Override
    public Page<Review> getActiveReviewsByUserId(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        return reviewRepository.findByUserAndIsActiveTrue(user, pageable);
    }

    @Override
    public Page<Review> getTopLikedReviews(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovieOrderByLikeCountDesc(movie, pageable);
    }

    @Override
    public Page<Review> getUserReviews(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        return reviewRepository.findByUser(user, pageable);
    }

    @Override
    public long countActiveReviewsByMovieId(Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.countByMovieAndIsActiveTrue(movie);
    }

    @Override
    public Page<Review> getMovieReviews(Long movieId, Pageable pageable) {
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.findByMovie(movie, pageable);
    }

    @Override
    public boolean hasUserReviewedMovie(Long userId, Long movieId) {
        User user = userService.getUser(userId);
        Movie movie = movieService.getMovie(movieId);
        return reviewRepository.existsByMovieAndUser(movie, user);
    }

    @Override
    public List<Review> getActiveReviewsByUserId(Long userId) {
        return reviewRepository.findActiveReviewsByUserId(userId);
    }
} 