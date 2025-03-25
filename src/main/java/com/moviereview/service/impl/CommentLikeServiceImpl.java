package com.moviereview.service.impl;

import com.moviereview.domain.comment.Comment;
import com.moviereview.domain.comment.CommentLike;
import com.moviereview.domain.comment.CommentLikeRepository;
import com.moviereview.domain.user.User;
import com.moviereview.exception.ResourceNotFoundException;
import com.moviereview.service.CommentLikeService;
import com.moviereview.service.CommentService;
import com.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;
    private final UserService userService;

    @Override
    @Transactional
    public CommentLike likeComment(Long commentId, Long userId) {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser(userId);

        if (hasUserLikedComment(commentId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }

        CommentLike commentLike = new CommentLike(comment, user);
        comment.incrementLikeCount();
        return commentLikeRepository.save(commentLike);
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserIdAndIsActiveTrue(commentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("좋아요를 찾을 수 없습니다."));

        Comment comment = commentLike.getComment();
        comment.decrementLikeCount();
        
        commentLike.deactivate();
        commentLikeRepository.save(commentLike);
    }

    @Override
    public boolean hasUserLikedComment(Long commentId, Long userId) {
        return commentLikeRepository.existsByCommentIdAndUserIdAndIsActiveTrue(commentId, userId);
    }

    @Override
    public long countCommentLikes(Long commentId) {
        return commentLikeRepository.countActiveLikesByCommentId(commentId);
    }
} 