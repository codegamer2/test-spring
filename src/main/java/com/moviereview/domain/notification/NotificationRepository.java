package com.moviereview.domain.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    long countUnreadNotifications(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.type = :type ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("type") NotificationType type);

    @Query("DELETE FROM Notification n WHERE n.user.id = :userId AND n.isRead = true")
    void deleteReadNotifications(@Param("userId") Long userId);
} 