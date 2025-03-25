# 🎬 무비리뷰 (MovieReview)

## 프로젝트 소개
무비리뷰는 사용자들이 영화에 대한 리뷰를 작성하고 공유할 수 있는 웹 애플리케이션입니다.

## 기술 스택
- **Backend**
  - Java 21
  - Spring Boot 3.4.4
  - Spring Data JPA
  - Spring Security
  - PostgreSQL (개발용)
  - PostgreSQL (운영용)
  - Maven

- **Frontend**
  - Thymeleaf
  - JavaScript
  - Typescript
  - ShadCN
  - TailwindCSS

## 주요 기능 (시나리오 기반)

1. **첫 방문 & 회원가입**
   - 서비스 소개 페이지 확인
   - 회원가입 선택
     - 이메일/비밀번호로 직접 가입
     - 소셜 계정으로 간편 가입 (Google/Naver/Kakao)
   - 기본 프로필 설정
     - 닉네임 설정
     - 프로필 이미지 업로드 (선택)
     - 관심 영화 장르 선택

2. **로그인 & 메인 화면**
   - 로그인 방식 선택
     - 이메일/비밀번호 로그인
     - 소셜 로그인
     - 자동 로그인 설정
   - 메인 화면 구성
     - 추천 영화 목록
     - 실시간 인기 리뷰
     - 팔로우 중인 사용자의 최신 리뷰
     - 관심 장르의 새로운 영화

3. **영화 탐색 & 상세정보**
   - 영화 검색
     - 제목/감독/배우 검색
     - 장르별 필터링
     - 개봉연도별 분류
   - 영화 상세 페이지
     - 기본 정보 (제목, 감독, 출연진, 개봉일 등)
     - 시놉시스
     - 예고편
     - 평균 별점
     - 리뷰 미리보기

4. **리뷰 작성 & 상호작용**
   - 리뷰 작성
     - 별점 평가 (1~5점)
     - 리뷰 제목과 내용 작성
     - 스포일러 포함 여부 표시
     - 이미지/동영상 첨부 (선택)
   - 리뷰 상호작용
     - 좋아요/싫어요 표시
     - 댓글 작성
     - 대댓글로 토론 참여
     - 리뷰 공유하기

5. **커뮤니티 활동**
   - 사용자 팔로우
     - 관심있는 리뷰어 팔로우
     - 팔로잉/팔로워 목록 관리
   - 알림 기능
     - 새 리뷰 알림
     - 댓글 알림
     - 좋아요 알림
   - 사용자 프로필
     - 작성한 리뷰 모아보기
     - 좋아요한 리뷰 목록
     - 활동 통계 (총 리뷰 수, 평균 별점 등)

각 기능의 상세 구현 계획

## 프로젝트 구조
```
src
├── main
│   ├── java/com/moviereview
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── domain
│   │   ├── dto
│   │   ├── config
│   │   └── exception
│   └── resources
│       ├── static
│       ├── templates
│       └── application.yml
└── test
    └── java/com/moviereview
```

## API 엔드포인트
- `GET /api/movies`: 영화 목록 조회
- `GET /api/movies/{id}`: 영화 상세 정보
- `POST /api/reviews`: 리뷰 작성
- `PUT /api/reviews/{id}`: 리뷰 수정
- `DELETE /api/reviews/{id}`: 리뷰 삭제

## 데이터베이스 스키마
### Movie
- id (PK)
- title
- description
- releaseDate
- genre
- director
- actors

### Review
- id (PK)
- movieId (FK)
- userId (FK)
- content
- rating
- createdAt
- updatedAt

### User
- id (PK)
- username
- email
- password
- role
- createdAt

## 실행 방법
1. 저장소 클론
```bash
git clone https://github.com/username/moviereview.git
```

2. 데이터베이스 설정
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: test
    password: test
```

3. 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

## 테스트
```bash
./mvnw test
```

## 라이선스
MIT License

## 기여 방법
1. Fork the Project
2. Create your Feature Branch
3. Commit your Changes
4. Push to the Branch
5. Open a Pull Request
