package com.moviereview.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String profileImage;

    @Column(nullable = false)
    private boolean emailVerified;

    private String verificationToken;

    private int reviewCount;

    private int commentCount;

    private int followerCount;

    private int followingCount;

    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String username, UserRole role, String profileImage) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.profileImage = profileImage;
        this.emailVerified = false;
        this.reviewCount = 0;
        this.commentCount = 0;
        this.followerCount = 0;
        this.followingCount = 0;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public void updateProfile(String username, String profileImage) {
        if (username != null) {
            this.username = username;
        }
        this.profileImage = profileImage;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.verificationToken = null;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerified;
    }
} 