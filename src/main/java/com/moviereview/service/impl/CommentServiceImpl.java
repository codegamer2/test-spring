package com.moviereview.service.impl;

import com.moviereview.domain.comment.Comment;
import com.moviereview.domain.comment.CommentRepository;
import com.moviereview.domain.review.Review;
import com.moviereview.domain.user.User;
import com.moviereview.dto.comment.CommentCreateRequest;
import com.moviereview.dto.comment.CommentUpdateRequest;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.service.CommentService;
import com.moviereview.service.ReviewService;
import com.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    @Override
    @Transactional
    public Comment createComment(Long userId, CommentCreateRequest request) {
        User user = userService.getUser(userId);
        Review review = reviewService.getReview(request.getReviewId());

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = getComment(request.getParentId());
            if (!parent.getReview().getId().equals(review.getId())) {
                throw new IllegalStateException("부모 댓글이 해당 리뷰의 댓글이 아닙니다.");
            }
        }

        Comment comment = Comment.builder()
                .review(review)
                .user(user)
                .content(request.getContent())
                .parent(parent)
                .build();

        Comment savedComment = commentRepository.save(comment);
        if (parent != null) {
            parent.addReply(savedComment);
        }
        review.incrementCommentCount();
        return savedComment;
    }

    @Override
    @Transactional
    public Comment updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = getComment(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(request.getContent());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }

        if (comment.getParent() != null) {
            comment.getParent().removeReply(comment);
        }
        commentRepository.delete(comment);
        comment.getReview().decrementCommentCount();
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
    }

    @Override
    public Page<Comment> getCommentsByReview(Long reviewId, Pageable pageable) {
        Review review = reviewService.getReview(reviewId);
        return commentRepository.findByReview(review, pageable);
    }

    @Override
    public Page<Comment> getCommentsByUser(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        return commentRepository.findByUser(user, pageable);
    }

    @Override
    public List<Comment> getRepliesByComment(Long commentId) {
        Comment comment = getComment(commentId);
        return commentRepository.findByParent(comment);
    }

    @Override
    public Page<Comment> getRootCommentsByReview(Long reviewId, Pageable pageable) {
        Review review = reviewService.getReview(reviewId);
        return commentRepository.findRootCommentsByReview(review, pageable);
    }

    @Override
    public Page<Comment> getCommentsByReviewOrderByLikeCount(Long reviewId, Pageable pageable) {
        Review review = reviewService.getReview(reviewId);
        return commentRepository.findByReviewOrderByLikeCountDesc(review, pageable);
    }

    @Override
    public Page<Comment> getRootCommentsByReviewOrderByCreatedAt(Long reviewId, Pageable pageable) {
        Review review = reviewService.getReview(reviewId);
        return commentRepository.findRootCommentsByReviewOrderByCreatedAtDesc(review, pageable);
    }

    @Override
    @Transactional
    public void likeComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        comment.incrementLikeCount();
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void unlikeComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        comment.decrementLikeCount();
        commentRepository.save(comment);
    }

    @Override
    public boolean hasUserLikedComment(Long userId, Long commentId) {
        // TODO: 구현 필요
        return false;
    }
} 