package com.moviereview.repository;

import com.moviereview.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByVerificationToken(String token);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> searchUsers(String keyword);

    @Query("SELECT u FROM User u JOIN u.followers f WHERE f.id = :userId")
    Page<User> findFollowingByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.following f WHERE f.id = :userId")
    Page<User> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u JOIN u.followers f WHERE f.id = :userId")
    long countFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM User u JOIN u.following f WHERE f.id = :userId")
    long countFollowingByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u ORDER BY u.reviewCount DESC")
    Page<User> findTopReviewers(Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.followerCount DESC")
    Page<User> findTopInfluencers(Pageable pageable);
} 