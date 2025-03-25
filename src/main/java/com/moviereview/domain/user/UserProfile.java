package com.moviereview.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String nickname;

    private String bio;

    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "user_favorite_genres", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "genre")
    private List<String> favoriteGenres = new ArrayList<>();

    private int reviewCount;

    private int commentCount;

    private int followerCount;

    private int followingCount;

    private double averageRating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public UserProfile(User user, String nickname, String bio, String profileImageUrl, List<String> favoriteGenres) {
        this.user = user;
        this.nickname = nickname;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.favoriteGenres = favoriteGenres != null ? favoriteGenres : new ArrayList<>();
        this.reviewCount = 0;
        this.commentCount = 0;
        this.followerCount = 0;
        this.followingCount = 0;
        this.averageRating = 0.0;
    }

    public void updateProfile(String nickname, String bio, String profileImageUrl, List<String> favoriteGenres) {
        this.nickname = nickname;
        if (bio != null) {
            this.bio = bio;
        }
        this.profileImageUrl = profileImageUrl;
        if (favoriteGenres != null) {
            this.favoriteGenres = favoriteGenres;
        }
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }

    public void decrementReviewCount() {
        if (this.reviewCount > 0) {
            this.reviewCount--;
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

    public void incrementFollowerCount() {
        this.followerCount++;
    }

    public void decrementFollowerCount() {
        if (this.followerCount > 0) {
            this.followerCount--;
        }
    }

    public void incrementFollowingCount() {
        this.followingCount++;
    }

    public void decrementFollowingCount() {
        if (this.followingCount > 0) {
            this.followingCount--;
        }
    }

    public void updateAverageRating(double newRating) {
        this.averageRating = newRating;
    }
} 