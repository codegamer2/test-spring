package com.moviereview.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotNull(message = "리뷰 ID는 필수입니다.")
    private Long reviewId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    private Long parentId;
} 