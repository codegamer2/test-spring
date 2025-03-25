package com.moviereview.controller;

import com.moviereview.domain.user.User;
import com.moviereview.dto.user.UserCreateRequest;
import com.moviereview.dto.user.UserProfileUpdateRequest;
import com.moviereview.dto.user.UserResponse;
import com.moviereview.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "사용자 생성")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request.getEmail(), request.getPassword(), request.getUsername());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @Operation(summary = "사용자 프로필 수정")
    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public ResponseEntity<UserResponse> updateUserProfile(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        User user = userService.updateUser(userId, request.getUsername(), request.getProfileImage());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @Operation(summary = "사용자 팔로우")
    @PostMapping("/{userId}/follow/{targetId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public ResponseEntity<Void> followUser(
            @Parameter(description = "팔로우하는 사용자 ID") @PathVariable Long userId,
            @Parameter(description = "팔로우할 사용자 ID") @PathVariable Long targetId) {
        userService.followUser(userId, targetId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 언팔로우")
    @DeleteMapping("/{userId}/follow/{targetId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public ResponseEntity<Void> unfollowUser(
            @Parameter(description = "언팔로우하는 사용자 ID") @PathVariable Long userId,
            @Parameter(description = "언팔로우할 사용자 ID") @PathVariable Long targetId) {
        userService.unfollowUser(userId, targetId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/{userId}/following")
    public ResponseEntity<Page<UserResponse>> getFollowingList(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            Pageable pageable) {
        Page<User> following = userService.getFollowingList(userId, pageable);
        return ResponseEntity.ok(following.map(UserResponse::from));
    }

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<UserResponse>> getFollowerList(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            Pageable pageable) {
        Page<User> followers = userService.getFollowerList(userId, pageable);
        return ResponseEntity.ok(followers.map(UserResponse::from));
    }

    @Operation(summary = "팔로잉 수 조회")
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCount(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.countFollowing(userId));
    }

    @Operation(summary = "팔로워 수 조회")
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowerCount(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.countFollowers(userId));
    }

    @Operation(summary = "인기 리뷰어 목록 조회")
    @GetMapping("/top/reviewers")
    public ResponseEntity<Page<UserResponse>> getTopReviewers(Pageable pageable) {
        Page<User> topReviewers = userService.getTopReviewers(pageable);
        return ResponseEntity.ok(topReviewers.map(UserResponse::from));
    }

    @Operation(summary = "인기 인플루언서 목록 조회")
    @GetMapping("/top/influencers")
    public ResponseEntity<Page<UserResponse>> getTopInfluencers(Pageable pageable) {
        Page<User> topInfluencers = userService.getTopInfluencers(pageable);
        return ResponseEntity.ok(topInfluencers.map(UserResponse::from));
    }
} 