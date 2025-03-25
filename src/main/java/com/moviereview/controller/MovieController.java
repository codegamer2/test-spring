package com.moviereview.controller;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.movie.ReleaseStatus;
import com.moviereview.dto.movie.MovieCreateRequest;
import com.moviereview.dto.movie.MovieResponse;
import com.moviereview.dto.movie.MovieUpdateRequest;
import com.moviereview.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "영화", description = "영화 관련 API")
@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "영화 생성", description = "새로운 영화를 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieCreateRequest request) {
        Movie movie = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MovieResponse.from(movie));
    }

    @Operation(summary = "영화 수정", description = "기존 영화 정보를 수정합니다.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> updateMovie(
            @Parameter(description = "영화 ID") @PathVariable Long id,
            @Valid @RequestBody MovieUpdateRequest request) {
        Movie movie = movieService.updateMovie(id, request);
        return ResponseEntity.ok(MovieResponse.from(movie));
    }

    @Operation(summary = "영화 삭제", description = "영화를 삭제합니다.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@Parameter(description = "영화 ID") @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "영화 조회", description = "영화 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovie(@Parameter(description = "영화 ID") @PathVariable Long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.ok(MovieResponse.from(movie));
    }

    @Operation(summary = "영화 검색", description = "키워드로 영화를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<Page<MovieResponse>> searchMovies(
            @Parameter(description = "검색 키워드") @RequestParam String keyword,
            Pageable pageable) {
        Page<Movie> movies = movieService.searchMovies(keyword, pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "개봉일 기준 영화 조회", description = "특정 기간에 개봉한 영화를 조회합니다.")
    @GetMapping("/release-date")
    public ResponseEntity<Page<MovieResponse>> getMoviesByReleaseDate(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate,
            Pageable pageable) {
        Page<Movie> movies = movieService.getMoviesByReleaseDate(startDate, endDate, pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "개봉 상태별 영화 조회", description = "개봉 상태별로 영화를 조회합니다.")
    @GetMapping("/status")
    public ResponseEntity<Page<MovieResponse>> getMoviesByStatus(
            @Parameter(description = "개봉 상태") @RequestParam ReleaseStatus status,
            Pageable pageable) {
        Page<Movie> movies = movieService.getMoviesByStatus(status, pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "장르별 영화 조회", description = "장르별로 영화를 조회합니다.")
    @GetMapping("/genre")
    public ResponseEntity<Page<MovieResponse>> getMoviesByGenre(
            @Parameter(description = "장르") @RequestParam String genre,
            Pageable pageable) {
        Page<Movie> movies = movieService.getMoviesByGenre(genre, pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "평점 기준 영화 조회", description = "최소 평점 이상의 영화를 조회합니다.")
    @GetMapping("/rating")
    public ResponseEntity<Page<MovieResponse>> getMoviesByRating(
            @Parameter(description = "최소 평점") @RequestParam double minRating,
            Pageable pageable) {
        Page<Movie> movies = movieService.getMoviesByRating(minRating, pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "리뷰 많은 영화 조회", description = "리뷰가 많은 순으로 영화를 조회합니다.")
    @GetMapping("/top-reviewed")
    public ResponseEntity<Page<MovieResponse>> getTopReviewedMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getTopReviewedMovies(pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "현재 상영 중인 영화 조회", description = "현재 상영 중인 영화를 조회합니다.")
    @GetMapping("/released")
    public ResponseEntity<Page<MovieResponse>> getReleasedMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getReleasedMovies(pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }

    @Operation(summary = "개봉 예정 영화 조회", description = "개봉 예정인 영화를 조회합니다.")
    @GetMapping("/upcoming")
    public ResponseEntity<Page<MovieResponse>> getUpcomingMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getUpcomingMovies(pageable);
        return ResponseEntity.ok(movies.map(MovieResponse::from));
    }
} 