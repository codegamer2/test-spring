# 로그인 & 메인 화면 상세 계획

## 1. 로그인 시스템

### 1.1 로그인 방식
- **일반 로그인**
  ```java
  public class LoginRequest {
      @NotBlank
      @Email
      private String email;

      @NotBlank
      private String password;

      private boolean rememberMe;
  }
  ```

- **소셜 로그인**
  - Google
  - Naver
  - Kakao

- **자동 로그인**
  - JWT Refresh Token 활용
  - 토큰 만료 시간 설정
  - 보안 강화

### 1.2 로그인 프로세스
1. 사용자 인증
2. JWT 토큰 발급
3. 세션 관리
4. 권한 검증

## 2. 메인 화면 구성

### 2.1 추천 영화 목록
- **추천 알고리즘**
  ```java
  public class MovieRecommendation {
      private Long movieId;
      private String title;
      private String posterUrl;
      private Double rating;
      private List<String> genres;
      private String recommendationReason;
  }
  ```

- **추천 기준**
  - 사용자 관심 장르
  - 최근 본 영화
  - 인기 영화
  - 개봉 예정 영화

### 2.2 실시간 인기 리뷰
- **인기 리뷰 선정 기준**
  - 좋아요 수
  - 댓글 수
  - 조회수
  - 작성 시간

- **리뷰 미리보기**
  ```java
  public class PopularReview {
      private Long reviewId;
      private String title;
      private String content;
      private Integer rating;
      private String movieTitle;
      private String userName;
      private Integer likeCount;
      private Integer commentCount;
      private LocalDateTime createdAt;
  }
  ```

### 2.3 팔로우 중인 사용자의 최신 리뷰
- **팔로우 시스템**
  ```java
  public class Follow {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne
      private User follower;

      @ManyToOne
      private User following;

      private LocalDateTime createdAt;
  }
  ```

- **피드 정렬**
  - 최신순
  - 인기순
  - 장르별

### 2.4 관심 장르의 새로운 영화
- **장르 기반 필터링**
  ```java
  public class GenreBasedMovie {
      private Long movieId;
      private String title;
      private String posterUrl;
      private LocalDate releaseDate;
      private List<String> genres;
      private Double rating;
      private Integer reviewCount;
  }
  ```

## 3. API 엔드포인트

### 3.1 로그인 관련
```
POST /api/auth/login
POST /api/auth/logout
POST /api/auth/refresh-token
GET /api/auth/me
```

### 3.2 추천 관련
```
GET /api/recommendations
GET /api/recommendations/personal
GET /api/recommendations/popular
```

### 3.3 팔로우 관련
```
POST /api/follows
DELETE /api/follows/{userId}
GET /api/follows/following
GET /api/follows/followers
```

## 4. 데이터베이스 스키마

### 4.1 Follow 테이블
```sql
CREATE TABLE follows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES users(id),
    FOREIGN KEY (following_id) REFERENCES users(id),
    UNIQUE KEY unique_follow (follower_id, following_id)
);
```

### 4.2 UserViewHistory 테이블
```sql
CREATE TABLE user_view_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
```

## 5. 캐싱 전략

### 5.1 Redis 캐싱
- **캐시 키 구조**
  ```
  recommendations:user:{userId}
  popular:reviews
  following:reviews:{userId}
  genre:movies:{genre}
  ```

- **캐시 만료 시간**
  - 추천 목록: 1시간
  - 인기 리뷰: 30분
  - 팔로우 피드: 15분
  - 장르별 영화: 1시간

### 5.2 캐시 갱신 전략
- **주기적 갱신**
  - 인기 리뷰: 30분마다
  - 추천 목록: 1시간마다
  - 장르별 영화: 1시간마다

- **이벤트 기반 갱신**
  - 새 리뷰 작성
  - 좋아요/댓글 추가
  - 팔로우 상태 변경

## 6. 성능 최적화

### 6.1 데이터베이스 최적화
- **인덱스 설정**
  ```sql
  CREATE INDEX idx_follows_follower ON follows(follower_id);
  CREATE INDEX idx_follows_following ON follows(following_id);
  CREATE INDEX idx_reviews_created_at ON reviews(created_at);
  CREATE INDEX idx_movies_release_date ON movies(release_date);
  ```

- **쿼리 최적화**
  - N+1 문제 해결
  - 페이징 처리
  - 조인 최적화

### 6.2 프론트엔드 최적화
- **이미지 최적화**
  - 레이지 로딩
  - 이미지 리사이징
  - WebP 포맷 지원

- **데이터 페칭**
  - 무한 스크롤
  - 데이터 프리페칭
  - 스켈레톤 로딩

## 7. 모니터링

### 7.1 성능 모니터링
- API 응답 시간
- 캐시 히트율
- 데이터베이스 쿼리 성능
- 프론트엔드 로딩 시간

### 7.2 사용자 행동 분석
- 페이지 조회수
- 클릭 이벤트
- 스크롤 깊이
- 체류 시간 