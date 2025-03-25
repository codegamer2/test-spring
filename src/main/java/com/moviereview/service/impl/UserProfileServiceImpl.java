package com.moviereview.service.impl;

import com.moviereview.domain.user.User;
import com.moviereview.domain.user.UserProfile;
import com.moviereview.repository.UserProfileRepository;
import com.moviereview.repository.UserRepository;
import com.moviereview.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 프로필을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public UserProfile updateProfile(Long userId, String nickname, String bio, String profileImage, List<String> favoriteGenres) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateProfile(nickname, bio, profileImage, favoriteGenres);
        return userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void incrementReviewCount(Long userId) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.incrementReviewCount();
        userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void incrementCommentCount(Long userId) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.incrementCommentCount();
        userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void incrementFollowerCount(Long userId) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.incrementFollowerCount();
        userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void incrementFollowingCount(Long userId) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.incrementFollowingCount();
        userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void updateAverageRating(Long userId, double rating) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateAverageRating(rating);
        userProfileRepository.save(userProfile);
    }
} 