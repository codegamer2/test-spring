package com.moviereview.domain.movie;

public enum ReleaseStatus {
    UPCOMING("개봉 예정"),
    RELEASED("개봉"),
    CLOSED("종료");

    private final String description;

    ReleaseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 