package com.moviereview.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    @Query("SELECT up FROM UserProfile up WHERE up.reviewCount > 0 ORDER BY up.reviewCount DESC")
    List<UserProfile> findTopReviewers();

    @Query("SELECT up FROM UserProfile up WHERE up.followerCount > 0 ORDER BY up.followerCount DESC")
    List<UserProfile> findTopInfluencers();

    @Query("SELECT up FROM UserProfile up WHERE up.averageRating > 0 ORDER BY up.averageRating DESC")
    List<UserProfile> findTopRaters();

    @Query("SELECT up FROM UserProfile up WHERE up.user.favoriteGenres LIKE %:genre%")
    List<UserProfile> findByFavoriteGenre(@Param("genre") String genre);
} 