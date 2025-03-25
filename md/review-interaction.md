# 리뷰 작성 & 상호작용 상세 계획

## 1. 리뷰 작성 시스템

### 1.1 리뷰 데이터 모델
```java
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    private boolean hasSpoiler;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private List<String> videos;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewLike> likes;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewDislike> dislikes;
}
```

### 1.2 리뷰 작성 프로세스
- **기본 정보 입력**
  - 제목
  - 내용
  - 별점 (1-5점)
  - 스포일러 여부

- **미디어 첨부**
  - 이미지 업로드
  - 동영상 첨부
  - 미디어 미리보기

- **검증 규칙**
  - 최소 글자 수
  - 최대 이미지 수
  - 허용된 파일 형식

## 2. 리뷰 상호작용

### 2.1 좋아요/싫어요 시스템
```java
@Entity
@Table(name = "review_likes")
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
```

### 2.2 댓글 시스템
```java
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies;

    @Column(length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

### 2.3 공유 기능
- **공유 옵션**
  - URL 복사
  - SNS 공유
  - 임베드 코드

## 3. API 엔드포인트

### 3.1 리뷰 관련
```
POST /api/reviews
GET /api/reviews/{id}
PUT /api/reviews/{id}
DELETE /api/reviews/{id}
GET /api/reviews/movie/{movieId}
GET /api/reviews/user/{userId}
```

### 3.2 상호작용 관련
```
POST /api/reviews/{id}/like
DELETE /api/reviews/{id}/like
POST /api/reviews/{id}/dislike
DELETE /api/reviews/{id}/dislike
POST /api/reviews/{id}/comments
GET /api/reviews/{id}/comments
PUT /api/comments/{id}
DELETE /api/comments/{id}
```

### 3.3 미디어 관련
```
POST /api/reviews/{id}/images
POST /api/reviews/{id}/videos
DELETE /api/reviews/{id}/media/{mediaId}
```

## 4. 데이터베이스 스키마

### 4.1 ReviewMedia 테이블
```sql
CREATE TABLE review_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL,
    media_type VARCHAR(20) NOT NULL,  -- image, video
    media_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES reviews(id)
);
```

### 4.2 Comment 테이블 확장
```sql
ALTER TABLE comments
ADD COLUMN parent_id BIGINT,
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE,
ADD FOREIGN KEY (parent_id) REFERENCES comments(id);
```

## 5. 성능 최적화

### 5.1 캐싱 전략
- **캐시 키**
  ```
  review:detail:{id}
  review:comments:{id}
  review:likes:{id}
  review:dislikes:{id}
  ```

- **캐시 만료**
  - 리뷰 상세: 1시간
  - 댓글 목록: 30분
  - 좋아요/싫어요: 15분

### 5.2 데이터베이스 최적화
- **인덱스 설정**
  ```sql
  CREATE INDEX idx_reviews_movie ON reviews(movie_id);
  CREATE INDEX idx_reviews_user ON reviews(user_id);
  CREATE INDEX idx_comments_review ON comments(review_id);
  CREATE INDEX idx_comments_parent ON comments(parent_id);
  CREATE INDEX idx_review_likes_review ON review_likes(review_id);
  CREATE INDEX idx_review_likes_user ON review_likes(user_id);
  ```

- **쿼리 최적화**
  - N+1 문제 해결
  - 페이징 처리
  - 조인 최적화

## 6. 알림 시스템

### 6.1 알림 유형
- **댓글 알림**
  - 새 댓글
  - 대댓글
  - 댓글 좋아요

- **리뷰 알림**
  - 좋아요
  - 공유
  - 신고

### 6.2 알림 전송
- **실시간 알림**
  - WebSocket
  - SSE (Server-Sent Events)

- **이메일 알림**
  - 알림 설정
  - 알림 템플릿

## 7. 모니터링

### 7.1 성능 모니터링
- API 응답 시간
- 데이터베이스 성능
- 캐시 히트율
- 실시간 알림 성능

### 7.2 사용자 행동 분석
- 리뷰 작성 통계
- 상호작용 통계
- 알림 클릭률
- 체류 시간 