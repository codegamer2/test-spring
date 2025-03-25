package com.moviereview.dto.comment;

import com.moviereview.domain.comment.Comment;
import com.moviereview.dto.user.UserResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private Long reviewId;
    private UserResponse user;
    private String content;
    private Long parentId;
    private List<CommentResponse> replies;
    private long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .reviewId(comment.getReview().getId())
                .user(UserResponse.from(comment.getUser()))
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .replies(comment.getReplies().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toList()))
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
} 