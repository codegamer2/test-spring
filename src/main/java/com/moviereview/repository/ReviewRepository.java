package com.moviereview.repository;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.review.Review;
import com.moviereview.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByMovie(Movie movie, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
    boolean existsByMovieAndUser(Movie movie, User user);

    @Query("SELECT r FROM Review r WHERE r.movie = :movie AND r.hasSpoiler = false")
    Page<Review> findNonSpoilerReviewsByMovie(@Param("movie") Movie movie, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.movie = :movie AND r.rating >= :minRating")
    Page<Review> findByMovieAndRatingGreaterThanEqual(
            @Param("movie") Movie movie, @Param("minRating") Integer minRating, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.movie = :movie ORDER BY r.likeCount DESC")
    Page<Review> findByMovieOrderByLikeCountDesc(@Param("movie") Movie movie, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.movie = :movie ORDER BY r.commentCount DESC")
    Page<Review> findByMovieOrderByCommentCountDesc(@Param("movie") Movie movie, Pageable pageable);

    Page<Review> findByMovieIdAndIsActiveTrueOrderByCreatedAtDesc(Long movieId, Pageable pageable);
    Page<Review> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<Review> findByMovieIdAndIsActiveTrue(Long movieId);

    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId AND r.isActive = true ORDER BY r.likeCount DESC")
    Page<Review> findTopLikedReviewsByMovieId(@Param("movieId") Long movieId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId AND r.isActive = true ORDER BY r.commentCount DESC")
    Page<Review> findTopCommentedReviewsByMovieId(@Param("movieId") Long movieId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.isActive = true ORDER BY r.createdAt DESC")
    List<Review> findActiveReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.movie.id = :movieId AND r.isActive = true")
    long countActiveReviewsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId AND r.isActive = true")
    Double calculateAverageRatingByMovieId(@Param("movieId") Long movieId);

    Optional<Review> findByMovieIdAndUserIdAndIsActiveTrue(Long movieId, Long userId);

    boolean existsByUserIdAndId(Long userId, Long id);
    Page<Review> findByUserAndIsActiveTrue(User user, Pageable pageable);
    long countByMovieAndIsActiveTrue(Movie movie);
    List<Review> findByMovieAndIsActiveTrue(Movie movie);

    List<Review> findByMovieIdOrderByCreatedAtDesc(Long movieId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Review> findByIdAndUserId(Long id, Long userId);
} 