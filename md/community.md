# 커뮤니티 활동 상세 계획

## 1. 팔로우 시스템

### 1.1 팔로우 데이터 모델
```java
@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isActive = true;
}
```

### 1.2 팔로우 기능
- **팔로우/언팔로우**
  - 팔로우 요청
  - 팔로우 수락/거절
  - 언팔로우

- **팔로우 목록**
  - 팔로잉 목록
  - 팔로워 목록
  - 추천 팔로우

## 2. 알림 시스템

### 2.1 알림 데이터 모델
```java
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(length = 500)
    private String content;

    private String link;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
```

### 2.2 알림 유형
```java
public enum NotificationType {
    NEW_REVIEW,          // 새 리뷰
    NEW_COMMENT,         // 새 댓글
    REPLY_TO_COMMENT,    // 댓글 답글
    LIKE_REVIEW,         // 리뷰 좋아요
    LIKE_COMMENT,        // 댓글 좋아요
    FOLLOW_USER,         // 새 팔로워
    MENTION_USER,        // 멘션
    SYSTEM_NOTICE        // 시스템 공지
}
```

### 2.3 알림 전송
- **실시간 알림**
  - WebSocket 연결
  - 알림 필터링
  - 알림 그룹화

- **이메일 알림**
  - 알림 설정
  - 이메일 템플릿
  - 발송 제한

## 3. 사용자 프로필

### 3.1 프로필 데이터 모델
```java
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bio;

    private String profileImage;

    @ElementCollection
    private List<String> favoriteGenres;

    private Integer reviewCount;

    private Integer followerCount;

    private Integer followingCount;

    private Double averageRating;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### 3.2 프로필 통계
- **활동 통계**
  - 총 리뷰 수
  - 평균 별점
  - 팔로워/팔로잉 수
  - 좋아요 받은 수

- **장르 통계**
  - 선호 장르
  - 장르별 리뷰 수
  - 장르별 평균 별점

## 4. API 엔드포인트

### 4.1 팔로우 관련
```
POST /api/follows
DELETE /api/follows/{userId}
GET /api/follows/following
GET /api/follows/followers
GET /api/follows/recommendations
```

### 4.2 알림 관련
```
GET /api/notifications
PUT /api/notifications/{id}/read
DELETE /api/notifications/{id}
PUT /api/notifications/settings
GET /api/notifications/unread-count
```

### 4.3 프로필 관련
```
GET /api/profiles/{userId}
PUT /api/profiles
GET /api/profiles/{userId}/reviews
GET /api/profiles/{userId}/likes
GET /api/profiles/{userId}/statistics
```

## 5. 데이터베이스 스키마

### 5.1 Notification 테이블
```sql
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    content VARCHAR(500),
    link VARCHAR(255),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 5.2 UserProfile 테이블
```sql
CREATE TABLE user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    bio TEXT,
    profile_image VARCHAR(255),
    review_count INT DEFAULT 0,
    follower_count INT DEFAULT 0,
    following_count INT DEFAULT 0,
    average_rating DOUBLE DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 6. 성능 최적화

### 6.1 캐싱 전략
- **캐시 키**
  ```
  profile:{userId}
  profile:reviews:{userId}
  profile:likes:{userId}
  notifications:unread:{userId}
  follows:following:{userId}
  follows:followers:{userId}
  ```

- **캐시 만료**
  - 프로필 정보: 1시간
  - 리뷰 목록: 30분
  - 알림 목록: 15분
  - 팔로우 목록: 1시간

### 6.2 데이터베이스 최적화
- **인덱스 설정**
  ```sql
  CREATE INDEX idx_notifications_user ON notifications(user_id);
  CREATE INDEX idx_notifications_type ON notifications(type);
  CREATE INDEX idx_notifications_created ON notifications(created_at);
  CREATE INDEX idx_follows_follower ON follows(follower_id);
  CREATE INDEX idx_follows_following ON follows(following_id);
  ```

- **쿼리 최적화**
  - 조인 최적화
  - 페이징 처리
  - 서브쿼리 최적화

## 7. 모니터링

### 7.1 성능 모니터링
- API 응답 시간
- WebSocket 연결 상태
- 알림 전송 성공률
- 캐시 히트율

### 7.2 사용자 행동 분석
- 팔로우 패턴
- 알림 클릭률
- 프로필 조회 통계
- 활동 시간대 분석 