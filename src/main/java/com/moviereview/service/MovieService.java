package com.moviereview.service;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.movie.ReleaseStatus;
import com.moviereview.dto.movie.MovieCreateRequest;
import com.moviereview.dto.movie.MovieUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MovieService {
    Movie createMovie(MovieCreateRequest request);
    Movie updateMovie(Long id, MovieUpdateRequest request);
    void deleteMovie(Long id);
    Movie getMovie(Long id);
    Page<Movie> searchMovies(String keyword, Pageable pageable);
    Page<Movie> getMoviesByReleaseDate(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Movie> getMoviesByStatus(ReleaseStatus status, Pageable pageable);
    Page<Movie> getMoviesByGenre(String genre, Pageable pageable);
    Page<Movie> getMoviesByRating(double minRating, Pageable pageable);
    Page<Movie> getTopReviewedMovies(Pageable pageable);
    Page<Movie> getReleasedMovies(Pageable pageable);
    Page<Movie> getUpcomingMovies(Pageable pageable);
    void updateMovieRating(Long id);
} 