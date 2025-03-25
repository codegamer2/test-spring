package com.moviereview.domain.movie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String originalTitle;
    private String englishTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReleaseStatus releaseStatus;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

    @Column(nullable = false)
    private String director;

    @ElementCollection
    @CollectionTable(name = "movie_actors", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "actor")
    private List<String> actors = new ArrayList<>();

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private long reviewCount = 0;

    @Column(nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
    @Builder
    public Movie(String title, String originalTitle, String englishTitle, String description,
                LocalDate releaseDate, ReleaseStatus releaseStatus, List<String> genres,
                String director, List<String> actors, String posterUrl, String trailerUrl) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.englishTitle = englishTitle;
        this.description = description;
        this.releaseDate = releaseDate;
        this.releaseStatus = releaseStatus;
        this.genres = genres != null ? genres : new ArrayList<>();
        this.director = director;
        this.actors = actors != null ? actors : new ArrayList<>();
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.rating = 0.0;
        this.reviewCount = 0;
        this.isActive = true;
    }

    public void updateRating(double newRating) {
        this.rating = newRating;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }

    public void decrementReviewCount() {
        if (this.reviewCount > 0) {
            this.reviewCount--;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setActors(List<String> actors) {
        this.actors = actors != null ? actors : new ArrayList<>();
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
} 