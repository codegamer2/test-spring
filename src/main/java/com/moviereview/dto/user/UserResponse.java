package com.moviereview.dto.user;

import com.moviereview.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String username;
    private String profileImage;
    private String role;
    private boolean emailVerified;
    private int reviewCount;
    private int commentCount;
    private int followerCount;
    private int followingCount;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.email = user.getEmail();
        response.username = user.getUsername();
        response.profileImage = user.getProfileImage();
        response.role = user.getRole().name();
        response.emailVerified = user.isEmailVerified();
        response.reviewCount = user.getReviewCount();
        response.commentCount = user.getCommentCount();
        response.followerCount = user.getFollowerCount();
        response.followingCount = user.getFollowingCount();
        return response;
    }
} 