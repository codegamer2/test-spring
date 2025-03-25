package com.moviereview.controller;

import com.moviereview.domain.comment.Comment;
import com.moviereview.dto.comment.CommentCreateRequest;
import com.moviereview.dto.comment.CommentResponse;
import com.moviereview.dto.comment.CommentUpdateRequest;
import com.moviereview.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "댓글", description = "댓글 관련 API")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새로운 댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CommentCreateRequest request) {
        Comment comment = commentService.createComment(userId, request);
        return CommentResponse.from(comment);
    }

    @Operation(summary = "댓글 수정", description = "기존 댓글을 수정합니다.")
    @PutMapping("/{id}")
    public CommentResponse updateComment(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "댓글 ID") @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request) {
        Comment comment = commentService.updateComment(userId, id, request);
        return CommentResponse.from(comment);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "댓글 ID") @PathVariable Long id) {
        commentService.deleteComment(userId, id);
    }

    @Operation(summary = "댓글 조회", description = "댓글 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public CommentResponse getComment(@Parameter(description = "댓글 ID") @PathVariable Long id) {
        Comment comment = commentService.getComment(id);
        return CommentResponse.from(comment);
    }

    @Operation(summary = "리뷰별 댓글 조회", description = "특정 리뷰의 댓글 목록을 조회합니다.")
    @GetMapping("/review/{reviewId}")
    public Page<CommentResponse> getCommentsByReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            Pageable pageable) {
        return commentService.getCommentsByReview(reviewId, pageable)
                .map(CommentResponse::from);
    }

    @Operation(summary = "사용자별 댓글 조회", description = "특정 사용자의 댓글 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public Page<CommentResponse> getCommentsByUser(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            Pageable pageable) {
        return commentService.getCommentsByUser(userId, pageable)
                .map(CommentResponse::from);
    }

    @Operation(summary = "댓글의 답글 조회", description = "특정 댓글의 답글 목록을 조회합니다.")
    @GetMapping("/{id}/replies")
    public List<CommentResponse> getRepliesByComment(
            @Parameter(description = "댓글 ID") @PathVariable Long id) {
        return commentService.getRepliesByComment(id).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "리뷰의 루트 댓글 조회", description = "특정 리뷰의 루트 댓글 목록을 조회합니다.")
    @GetMapping("/review/{reviewId}/root")
    public Page<CommentResponse> getRootCommentsByReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            Pageable pageable) {
        return commentService.getRootCommentsByReview(reviewId, pageable)
                .map(CommentResponse::from);
    }

    @Operation(summary = "좋아요 순 댓글 조회", description = "좋아요가 많은 순으로 댓글을 조회합니다.")
    @GetMapping("/review/{reviewId}/likes")
    public Page<CommentResponse> getCommentsByReviewOrderByLikeCount(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            Pageable pageable) {
        return commentService.getCommentsByReviewOrderByLikeCount(reviewId, pageable)
                .map(CommentResponse::from);
    }

    @Operation(summary = "최신순 루트 댓글 조회", description = "최신순으로 루트 댓글을 조회합니다.")
    @GetMapping("/review/{reviewId}/root/latest")
    public Page<CommentResponse> getRootCommentsByReviewOrderByCreatedAt(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            Pageable pageable) {
        return commentService.getRootCommentsByReviewOrderByCreatedAt(reviewId, pageable)
                .map(CommentResponse::from);
    }

    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요를 표시합니다.")
    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeComment(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "댓글 ID") @PathVariable Long id) {
        commentService.likeComment(userId, id);
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글의 좋아요를 취소합니다.")
    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeComment(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "댓글 ID") @PathVariable Long id) {
        commentService.unlikeComment(userId, id);
    }
} 