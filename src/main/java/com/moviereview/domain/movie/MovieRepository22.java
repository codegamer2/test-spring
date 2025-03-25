package com.moviereview.domain.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository22 extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    boolean existsByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% OR m.originalTitle LIKE %:keyword% OR m.englishTitle LIKE %:keyword%")
    Page<Movie> searchMovies(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate BETWEEN :startDate AND :endDate ORDER BY m.releaseDate DESC")
    Page<Movie> findByReleaseDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseStatus = :status ORDER BY m.releaseDate DESC")
    Page<Movie> findByReleaseStatus(@Param("status") ReleaseStatus status, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE :genre MEMBER OF m.genres ORDER BY m.releaseDate DESC")
    Page<Movie> findByGenre(@Param("genre") String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.averageRating >= :rating ORDER BY m.averageRating DESC")
    Page<Movie> findByRatingGreaterThanEqual(@Param("rating") Double rating, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.reviewCount > 0 ORDER BY m.reviewCount DESC")
    List<Movie> findTopReviewedMovies();

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= CURRENT_DATE ORDER BY m.releaseDate DESC")
    Page<Movie> findReleasedMovies(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate > CURRENT_DATE ORDER BY m.releaseDate ASC")
    Page<Movie> findUpcomingMovies(Pageable pageable);

    Page<Movie> findByTitleContainingOrOriginalTitleContainingOrEnglishTitleContaining(
            String title, String originalTitle, String englishTitle, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE :genre MEMBER OF m.genres")
    Page<Movie> findByGenresContaining(@Param("genre") String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.rating >= :minRating")
    Page<Movie> findByRatingGreaterThanEqual(@Param("minRating") double minRating, Pageable pageable);

    Page<Movie> findByOrderByReviewCountDesc(Pageable pageable);

    Page<Movie> findByReleaseStatusAndReleaseDateLessThanEqual(
            ReleaseStatus status, LocalDate date, Pageable pageable);

    Page<Movie> findByReleaseStatusAndReleaseDateGreaterThan(
            ReleaseStatus status, LocalDate date, Pageable pageable);
} 