package com.moviereview.service;

import com.moviereview.domain.comment.Comment;
import com.moviereview.dto.comment.CommentCreateRequest;
import com.moviereview.dto.comment.CommentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment createComment(Long userId, CommentCreateRequest request);
    Comment updateComment(Long userId, Long commentId, CommentUpdateRequest request);
    void deleteComment(Long userId, Long commentId);
    Comment getComment(Long commentId);
    Page<Comment> getCommentsByReview(Long reviewId, Pageable pageable);
    Page<Comment> getCommentsByUser(Long userId, Pageable pageable);
    List<Comment> getRepliesByComment(Long commentId);
    Page<Comment> getRootCommentsByReview(Long reviewId, Pageable pageable);
    Page<Comment> getCommentsByReviewOrderByLikeCount(Long reviewId, Pageable pageable);
    Page<Comment> getRootCommentsByReviewOrderByCreatedAt(Long reviewId, Pageable pageable);
    void likeComment(Long userId, Long commentId);
    void unlikeComment(Long userId, Long commentId);
    boolean hasUserLikedComment(Long userId, Long commentId);
} 