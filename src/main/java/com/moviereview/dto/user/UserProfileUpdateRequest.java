package com.moviereview.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequest {

    @Size(min = 2, max = 20, message = "사용자명은 2자 이상 20자 이하여야 합니다.")
    private String username;

    private String profileImage;
} 