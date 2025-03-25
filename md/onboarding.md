# 첫 방문 & 회원가입 상세 계획

## 1. 서비스 소개 페이지

### 1.1 페이지 구성
- **헤더 섹션**
  - 서비스 로고
  - 회원가입/로그인 버튼
  - 다국어 지원 (한국어/영어)

- **메인 섹션**
  - 서비스 소개 영상/이미지
  - 주요 기능 소개
  - 사용자 통계 (가입자 수, 리뷰 수 등)

- **특징 섹션**
  - 영화 리뷰 작성
  - 커뮤니티 활동
  - 맞춤형 추천
  - 소셜 기능

### 1.2 UI/UX
- **반응형 디자인**
  - 모바일 최적화
  - 태블릿 지원
  - 데스크톱 레이아웃

- **인터랙션**
  - 스크롤 애니메이션
  - 호버 효과
  - 부드러운 전환 효과

## 2. 회원가입 프로세스

### 2.1 이메일/비밀번호 가입
- **입력 정보**
  ```java
  public class SignUpRequest {
      @NotBlank
      @Email
      private String email;

      @NotBlank
      @Size(min = 8, max = 20)
      private String password;

      @NotBlank
      @Size(min = 2, max = 20)
      private String username;

      private String profileImage;
      
      private List<MovieGenre> favoriteGenres;
  }
  ```

- **검증 규칙**
  - 이메일 형식 검증
  - 비밀번호 복잡도 검증
  - 사용자명 중복 체크
  - 필수 약관 동의

### 2.2 소셜 계정 가입
- **지원 서비스**
  - Google
  - Naver
  - Kakao

- **추가 정보 입력**
  - 사용자명 설정
  - 프로필 이미지 선택
  - 관심 장르 선택

### 2.3 프로필 설정
- **기본 정보**
  - 프로필 이미지 업로드
    - 이미지 크기 제한
    - 이미지 포맷 검증
    - 이미지 리사이징
  - 닉네임 설정
    - 중복 체크
    - 특수문자 제한
  - 관심 장르 선택
    - 다중 선택 가능
    - 최대 5개 선택

## 3. API 엔드포인트

### 3.1 회원가입 관련
```
POST /api/auth/signup
POST /api/auth/signup/social
GET /api/auth/check-username
GET /api/auth/check-email
POST /api/auth/verify-email
```

### 3.2 프로필 설정 관련
```
POST /api/users/profile-image
PUT /api/users/profile
PUT /api/users/favorite-genres
```

## 4. 데이터베이스 스키마

### 4.1 User 테이블 확장
```sql
ALTER TABLE users
ADD COLUMN profile_image_url VARCHAR(255),
ADD COLUMN favorite_genres JSON,
ADD COLUMN email_verified BOOLEAN DEFAULT FALSE,
ADD COLUMN verification_token VARCHAR(100),
ADD COLUMN verification_token_expiry TIMESTAMP;
```

### 4.2 UserGenre 테이블
```sql
CREATE TABLE user_genres (
    user_id BIGINT,
    genre VARCHAR(50),
    PRIMARY KEY (user_id, genre),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 5. 이메일 인증

### 5.1 이메일 템플릿
- **인증 이메일**
  - 인증 링크
  - 만료 시간
  - 서비스 로고
  - 안내 메시지

- **환영 이메일**
  - 가입 축하 메시지
  - 서비스 소개
  - 시작하기 가이드

### 5.2 인증 프로세스
1. 이메일 발송
2. 인증 링크 클릭
3. 토큰 검증
4. 이메일 인증 완료

## 6. 보안

### 6.1 비밀번호 정책
- 최소 8자 이상
- 대소문자 포함
- 숫자 포함
- 특수문자 포함

### 6.2 이메일 인증
- 인증 토큰 만료 시간: 24시간
- 재발송 제한: 3회/시간
- IP 기반 제한

## 7. 모니터링

### 7.1 회원가입 통계
- 일별 가입자 수
- 소셜 가입 비율
- 이메일 인증 완료율
- 장르별 선호도

### 7.2 에러 모니터링
- 가입 실패 원인
- 이메일 발송 실패
- 인증 토큰 오류
- 중복 가입 시도 