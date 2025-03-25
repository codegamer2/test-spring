package com.moviereview.service.impl;

import com.moviereview.domain.user.User;
import com.moviereview.domain.user.UserProfile;
import com.moviereview.domain.user.UserRole;
import com.moviereview.domain.user.Follow;
import com.moviereview.dto.user.UserResponse;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.exception.DuplicateResourceException;
import com.moviereview.repository.UserRepository;
import com.moviereview.repository.UserProfileRepository;
import com.moviereview.repository.FollowRepository;
import com.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(String email, String password, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .role(UserRole.USER)
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        userRepository.save(user);
        userProfileRepository.save(userProfile);

        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long userId, String username, String profileImage) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (username != null && !username.equals(user.getUsername())) {
            if (userRepository.existsByUsername(username)) {
                throw new DuplicateResourceException("Username already exists: " + username);
            }
        }

        user.updateProfile(username, profileImage);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUser(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return UserResponse.from(getUser(userId));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return UserResponse.from(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다.")));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void incrementReviewCount(Long userId) {
        User user = getUser(userId);
        user.incrementReviewCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void decrementReviewCount(Long userId) {
        User user = getUser(userId);
        user.decrementReviewCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void incrementCommentCount(Long userId) {
        User user = getUser(userId);
        user.incrementCommentCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void decrementCommentCount(Long userId) {
        User user = getUser(userId);
        user.decrementCommentCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void incrementFollowerCount(Long userId) {
        User user = getUser(userId);
        user.incrementFollowerCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void decrementFollowerCount(Long userId) {
        User user = getUser(userId);
        user.decrementFollowerCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void incrementFollowingCount(Long userId) {
        User user = getUser(userId);
        user.incrementFollowingCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void decrementFollowingCount(Long userId) {
        User user = getUser(userId);
        user.decrementFollowingCount();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Following user not found with id: " + followingId));

        if (follower.getFollowing().contains(following)) {
            throw new DuplicateResourceException("User already follows this user");
        }

        follower.getFollowing().add(following);
        following.getFollowers().add(follower);

        follower.incrementFollowingCount();
        following.incrementFollowerCount();

        userRepository.save(follower);
        userRepository.save(following);
    }

    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("User cannot unfollow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Following user not found with id: " + followingId));

        if (!follower.getFollowing().contains(following)) {
            throw new IllegalArgumentException("User does not follow this user");
        }

        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);

        follower.decrementFollowingCount();
        following.decrementFollowerCount();

        userRepository.save(follower);
        userRepository.save(following);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = getUser(followerId);
        User following = getUser(followingId);
        return follower.getFollowing().contains(following);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getFollowingList(Long userId, Pageable pageable) {
        return userRepository.findFollowingByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getFollowerList(Long userId, Pageable pageable) {
        return userRepository.findFollowersByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFollowers(Long userId) {
        return userRepository.countFollowersByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFollowing(Long userId) {
        return userRepository.countFollowingByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getTopReviewers(Pageable pageable) {
        return userRepository.findTopReviewers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getTopInfluencers(Pageable pageable) {
        return userRepository.findTopInfluencers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getFollowers(Long userId) {
        User user = getUser(userId);
        return new ArrayList<>(user.getFollowers());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getFollowing(Long userId) {
        User user = getUser(userId);
        return new ArrayList<>(user.getFollowing());
    }

    @Override
    @Transactional(readOnly = true)
    public int getFollowerCount(Long userId) {
        User user = getUser(userId);
        return user.getFollowerCount();
    }

    @Override
    @Transactional(readOnly = true)
    public int getFollowingCount(Long userId) {
        User user = getUser(userId);
        return user.getFollowingCount();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    @Override
    @Transactional
    public User verifyUser(String token) {
        return userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 인증 토큰입니다."));
    }

    @Override
    @Transactional
    public UserProfile createUserProfile(Long userId, String nickname, String bio,
                                       String profileImageUrl, List<String> favoriteGenres) {
        User user = getUserOrThrow(userId);
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .nickname(nickname)
                .bio(bio)
                .profileImageUrl(profileImageUrl)
                .favoriteGenres(favoriteGenres)
                .build();

        return userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public UserProfile updateUserProfile(Long userId, String nickname, String bio,
                                       String profileImageUrl, List<String> favoriteGenres) {
        UserProfile userProfile = getUserProfile(userId);
        userProfile.updateProfile(nickname, bio, profileImageUrl, favoriteGenres);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자 프로필을 찾을 수 없습니다."));
    }

    @Override
    public List<UserProfile> getTopReviewers() {
        return userProfileRepository.findTopReviewers();
    }

    @Override
    public List<UserProfile> getTopInfluencers() {
        return userProfileRepository.findTopInfluencers();
    }

    @Override
    public List<UserProfile> getTopRaters() {
        return userProfileRepository.findTopRaters();
    }

    @Override
    public List<UserProfile> getUsersByFavoriteGenre(String genre) {
        return userProfileRepository.findByFavoriteGenresContaining(genre);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }
} 