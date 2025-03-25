package com.moviereview.controller;

import com.moviereview.dto.user.EmailVerificationDto;
import com.moviereview.dto.user.EmailVerificationConfirmDto;
import com.moviereview.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/verification")
    public ResponseEntity<?> sendVerificationEmail(@Valid @RequestBody EmailVerificationDto request) {
        try {
            emailVerificationService.sendVerificationEmail(request);
            return ResponseEntity.ok().body(new ApiResponse(true, "인증번호가 전송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/verification/confirm")
    public ResponseEntity<?> confirmVerification(@Valid @RequestBody EmailVerificationConfirmDto request) {
        try {
            emailVerificationService.confirmVerification(request);
            return ResponseEntity.ok().body(new ApiResponse(true, "이메일 인증이 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    private record ApiResponse(boolean success, String message) {}
} 