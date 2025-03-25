package com.moviereview.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private String username;

    private String profileImage;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String newPassword;
} 