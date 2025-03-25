package com.moviereview.service;

import com.moviereview.domain.user.UserProfile;

import java.util.List;

public interface UserProfileService {
    UserProfile getUserProfile(Long userId);
    UserProfile updateProfile(Long userId, String nickname, String bio, String profileImage, List<String> favoriteGenres);
    void incrementReviewCount(Long userId);
    void incrementCommentCount(Long userId);
    void incrementFollowerCount(Long userId);
    void incrementFollowingCount(Long userId);
    void updateAverageRating(Long userId, double rating);
} 