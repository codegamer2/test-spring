package com.moviereview.service;

import com.moviereview.dto.user.EmailVerificationConfirmDto;
import com.moviereview.dto.user.EmailVerificationDto;
import com.moviereview.exception.EmailVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationService {
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String VERIFICATION_PREFIX = "email:verification:";
    private static final long VERIFICATION_EXPIRE_MINUTES = 3;

    @Transactional
    public void sendVerificationEmail(EmailVerificationDto request) {
        String email = request.getEmail();
        String verificationCode = generateVerificationCode();
        
        // Redis에 인증번호 저장
        String key = VERIFICATION_PREFIX + email;
        redisTemplate.opsForValue().set(key, verificationCode, VERIFICATION_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[무비리뷰] 이메일 인증번호");
        message.setText("인증번호: " + verificationCode + "\n" +
                       "인증번호는 3분간 유효합니다.\n" +
                       "본인이 요청하지 않은 경우 이 메일을 무시하셔도 됩니다.");
        
        mailSender.send(message);
    }

    @Transactional
    public void confirmVerification(EmailVerificationConfirmDto request) {
        String email = request.getEmail();
        String token = request.getToken();
        
        String key = VERIFICATION_PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(key);
        
        if (savedCode == null) {
            throw new EmailVerificationException("인증번호가 만료되었습니다.");
        }
        
        if (!savedCode.equals(token)) {
            throw new EmailVerificationException("잘못된 인증번호입니다.");
        }
        
        // 인증 성공 시 Redis에서 삭제
        redisTemplate.delete(key);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
} 