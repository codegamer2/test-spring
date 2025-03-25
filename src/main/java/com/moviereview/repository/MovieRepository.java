package com.moviereview.repository;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.movie.ReleaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);

    Page<Movie> findByTitleContainingOrOriginalTitleContainingOrEnglishTitleContaining(
            String title, String originalTitle, String englishTitle, Pageable pageable);

    Page<Movie> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Movie> findByReleaseStatus(ReleaseStatus status, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE :genre MEMBER OF m.genres")
    Page<Movie> findByGenresContaining(@Param("genre") String genre, Pageable pageable);

    Page<Movie> findByRatingGreaterThanEqual(double rating, Pageable pageable);

    Page<Movie> findByOrderByReviewCountDesc(Pageable pageable);

    Page<Movie> findByReleaseStatusAndReleaseDateLessThanEqual(
            ReleaseStatus status, LocalDate date, Pageable pageable);

    Page<Movie> findByReleaseStatusAndReleaseDateGreaterThan(
            ReleaseStatus status, LocalDate date, Pageable pageable);
} 