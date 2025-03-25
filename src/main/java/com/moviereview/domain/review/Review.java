package com.moviereview.domain.review;

import com.moviereview.domain.movie.Movie;
import com.moviereview.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "has_spoiler", nullable = false)
    private boolean hasSpoiler;

    @ElementCollection
    @CollectionTable(name = "review_media", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls = new ArrayList<>();

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        likeCount = 0;
        commentCount = 0;
        isActive = true;
    }
    @Builder
    public Review(Movie movie, User user, String title, String content, Integer rating, boolean hasSpoiler, List<String> mediaUrls) {
        this.movie = movie;
        this.user = user;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.hasSpoiler = hasSpoiler;
        this.mediaUrls = mediaUrls != null ? mediaUrls : new ArrayList<>();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateReview(String title, String content, Integer rating, boolean hasSpoiler, List<String> mediaUrls) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.hasSpoiler = hasSpoiler;
        this.mediaUrls = mediaUrls != null ? mediaUrls : new ArrayList<>();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }
} 