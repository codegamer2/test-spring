package com.moviereview.dto.movie;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.movie.ReleaseStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieResponse {
    private Long id;
    private String title;
    private String originalTitle;
    private String englishTitle;
    private String description;
    private LocalDate releaseDate;
    private ReleaseStatus releaseStatus;
    private List<String> genres;
    private String director;
    private List<String> actors;
    private String posterUrl;
    private String trailerUrl;
    private Double rating;
    private long reviewCount;
    private boolean isActive;

    public static MovieResponse from(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setOriginalTitle(movie.getOriginalTitle());
        response.setEnglishTitle(movie.getEnglishTitle());
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setReleaseStatus(movie.getReleaseStatus());
        response.setGenres(movie.getGenres());
        response.setDirector(movie.getDirector());
        response.setActors(movie.getActors());
        response.setPosterUrl(movie.getPosterUrl());
        response.setTrailerUrl(movie.getTrailerUrl());
        response.setRating(movie.getRating());
        response.setReviewCount(movie.getReviewCount());
        response.setActive(movie.isActive());
        return response;
    }
} 