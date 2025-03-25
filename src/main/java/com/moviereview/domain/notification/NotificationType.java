package com.moviereview.domain.notification;

public enum NotificationType {
    NEW_REVIEW,          // 새 리뷰
    NEW_COMMENT,         // 새 댓글
    REPLY_TO_COMMENT,    // 댓글 답글
    LIKE_REVIEW,         // 리뷰 좋아요
    LIKE_COMMENT,        // 댓글 좋아요
    FOLLOW_USER,         // 새 팔로워
    MENTION_USER,        // 멘션
    SYSTEM_NOTICE        // 시스템 공지
} 