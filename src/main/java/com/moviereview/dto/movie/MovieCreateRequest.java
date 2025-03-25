package com.moviereview.dto.movie;

import com.moviereview.domain.movie.ReleaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieCreateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String originalTitle;
    private String englishTitle;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @NotNull(message = "개봉일은 필수입니다.")
    private LocalDate releaseDate;

    @NotNull(message = "개봉 상태는 필수입니다.")
    private ReleaseStatus releaseStatus;

    private List<String> genres;

    @NotBlank(message = "감독은 필수입니다.")
    private String director;

    private List<String> actors;
    private String posterUrl;
    private String trailerUrl;
} 