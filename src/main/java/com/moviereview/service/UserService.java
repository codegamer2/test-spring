package com.moviereview.service;

import com.moviereview.domain.user.User;
import com.moviereview.domain.user.UserProfile;
import com.moviereview.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User createUser(String email, String password, String username);
    
    User updateUser(Long userId, String username, String profileImage);
    
    void updatePassword(Long userId, String currentPassword, String newPassword);
    
    UserResponse getUserById(Long userId);
    
    UserResponse getUserByEmail(String email);
    
    Page<UserResponse> getAllUsers(Pageable pageable);
    
    void deleteUser(Long userId);
    
    void incrementReviewCount(Long userId);
    
    void decrementReviewCount(Long userId);
    
    void incrementCommentCount(Long userId);
    
    void decrementCommentCount(Long userId);
    
    void incrementFollowerCount(Long userId);
    
    void decrementFollowerCount(Long userId);
    
    void incrementFollowingCount(Long userId);
    
    void decrementFollowingCount(Long userId);
    
    User getUser(Long userId);
    
    User getUserByUsername(String username);
    
    List<User> searchUsers(String keyword);
    
    User verifyUser(String token);
    
    UserProfile createUserProfile(Long userId, String nickname, String bio,
                                String profileImageUrl, List<String> favoriteGenres);
    
    UserProfile updateUserProfile(Long userId, String nickname, String bio,
                                String profileImageUrl, List<String> favoriteGenres);
    
    UserProfile getUserProfile(Long userId);
    
    List<UserProfile> getTopReviewers();
    
    List<UserProfile> getTopInfluencers();
    
    List<UserProfile> getTopRaters();
    
    List<UserProfile> getUsersByFavoriteGenre(String genre);
    
    void followUser(Long followerId, Long followingId);
    
    void unfollowUser(Long followerId, Long followingId);
    
    boolean isFollowing(Long followerId, Long followingId);
    
    List<User> getFollowers(Long userId);
    
    List<User> getFollowing(Long userId);
    
    int getFollowerCount(Long userId);
    
    int getFollowingCount(Long userId);
    
    Page<User> getFollowingList(Long userId, Pageable pageable);
    
    Page<User> getFollowerList(Long userId, Pageable pageable);
    
    long countFollowers(Long userId);
    
    long countFollowing(Long userId);
    
    Page<User> getTopReviewers(Pageable pageable);
    
    Page<User> getTopInfluencers(Pageable pageable);
} 