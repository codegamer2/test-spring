package com.moviereview.repository;

import com.moviereview.domain.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    @Query("SELECT up FROM UserProfile up ORDER BY up.reviewCount DESC")
    List<UserProfile> findTopReviewers();

    @Query("SELECT up FROM UserProfile up ORDER BY up.followerCount DESC")
    List<UserProfile> findTopInfluencers();

    @Query("SELECT up FROM UserProfile up ORDER BY up.averageRating DESC")
    List<UserProfile> findTopRaters();

    List<UserProfile> findByFavoriteGenresContaining(String genre);
} 