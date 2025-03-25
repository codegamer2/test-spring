package com.moviereview.service;

import com.moviereview.domain.comment.CommentLike;

public interface CommentLikeService {
    CommentLike likeComment(Long commentId, Long userId);
    
    void unlikeComment(Long commentId, Long userId);
    
    boolean hasUserLikedComment(Long commentId, Long userId);
    
    long countCommentLikes(Long commentId);
} 