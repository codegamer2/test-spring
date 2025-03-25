package com.moviereview.service.impl;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.movie.ReleaseStatus;
import com.moviereview.domain.review.Review;
import com.moviereview.dto.movie.MovieCreateRequest;
import com.moviereview.dto.movie.MovieUpdateRequest;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.repository.ReviewRepository;
import com.moviereview.repository.MovieRepository;
import com.moviereview.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Movie createMovie(MovieCreateRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .originalTitle(request.getOriginalTitle())
                .englishTitle(request.getEnglishTitle())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .releaseStatus(request.getReleaseStatus())
                .genres(request.getGenres())
                .director(request.getDirector())
                .actors(request.getActors())
                .posterUrl(request.getPosterUrl())
                .trailerUrl(request.getTrailerUrl())
                .build();
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovie(Long id, MovieUpdateRequest request) {
        Movie movie = getMovie(id);
        movie.setTitle(request.getTitle());
        movie.setOriginalTitle(request.getOriginalTitle());
        movie.setEnglishTitle(request.getEnglishTitle());
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setReleaseStatus(request.getReleaseStatus());
        movie.setGenres(request.getGenres());
        movie.setDirector(request.getDirector());
        movie.setActors(request.getActors());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        movie.deactivate();
        movieRepository.save(movie);
    }

    @Override
    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    @Override
    public Page<Movie> searchMovies(String keyword, Pageable pageable) {
        return movieRepository.findByTitleContainingOrOriginalTitleContainingOrEnglishTitleContaining(
                keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<Movie> getMoviesByReleaseDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return movieRepository.findByReleaseDateBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<Movie> getMoviesByStatus(ReleaseStatus status, Pageable pageable) {
        return movieRepository.findByReleaseStatus(status, pageable);
    }

    @Override
    public Page<Movie> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenresContaining(genre, pageable);
    }

    @Override
    public Page<Movie> getMoviesByRating(double minRating, Pageable pageable) {
        return movieRepository.findByRatingGreaterThanEqual(minRating, pageable);
    }

    @Override
    public Page<Movie> getTopReviewedMovies(Pageable pageable) {
        return movieRepository.findByOrderByReviewCountDesc(pageable);
    }

    @Override
    public Page<Movie> getReleasedMovies(Pageable pageable) {
        return movieRepository.findByReleaseStatusAndReleaseDateLessThanEqual(
                ReleaseStatus.RELEASED, LocalDate.now(), pageable);
    }

    @Override
    public Page<Movie> getUpcomingMovies(Pageable pageable) {
        return movieRepository.findByReleaseStatusAndReleaseDateGreaterThan(
                ReleaseStatus.UPCOMING, LocalDate.now(), pageable);
    }

    @Override
    @Transactional
    public void updateMovieRating(Long movieId) {
        Movie movie = getMovie(movieId);
        List<Review> reviews = reviewRepository.findByMovieAndIsActiveTrue(movie);
        
        if (reviews.isEmpty()) {
            movie.updateRating(0.0);
            return;
        }

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        movie.updateRating(averageRating);
        movieRepository.save(movie);
    }
} 