package com.moviereview.repository;

import com.moviereview.domain.user.Follow;
import com.moviereview.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    Optional<Follow> findByFollowerIdAndFollowingIdAndIsActiveTrue(Long followerId, Long followingId);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId AND f.isActive = true")
    Page<User> findActiveFollowingByUserId(Long userId, Pageable pageable);

    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId AND f.isActive = true")
    Page<User> findActiveFollowersByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId AND f.isActive = true")
    long countActiveFollowersByUserId(Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId AND f.isActive = true")
    long countActiveFollowingByUserId(Long userId);
} 