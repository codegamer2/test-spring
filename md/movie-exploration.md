# 영화 탐색 & 상세정보 상세 계획

## 1. 영화 검색 시스템

### 1.1 검색 기능
- **검색 요청**
  ```java
  public class MovieSearchRequest {
      private String keyword;
      private List<String> genres;
      private Integer year;
      private String director;
      private String actor;
      private Integer page;
      private Integer size;
      private String sortBy;  // rating, releaseDate, popularity
      private String sortOrder;  // asc, desc
  }
  ```

- **검색 결과**
  ```java
  public class MovieSearchResponse {
      private List<MovieSearchResult> movies;
      private Integer totalCount;
      private Integer currentPage;
      private Integer totalPages;
      private Integer pageSize;
  }
  ```

### 1.2 필터링 시스템
- **장르별 필터링**
  - 단일/다중 장르 선택
  - 장르 조합 검색
  - 장르별 통계

- **연도별 필터링**
  - 특정 연도
  - 연도 범위
  - 개봉 예정

### 1.3 정렬 옵션
- 평점순
- 개봉일순
- 인기순
- 리뷰 수순

## 2. 영화 상세 페이지

### 2.1 기본 정보
```java
public class MovieDetail {
    private Long id;
    private String title;
    private String originalTitle;
    private String englishTitle;
    private LocalDate releaseDate;
    private List<String> genres;
    private String director;
    private List<String> actors;
    private String synopsis;
    private String posterUrl;
    private List<String> stills;
    private List<String> trailers;
    private Double averageRating;
    private Integer reviewCount;
    private String country;
    private String language;
    private String runningTime;
    private String ageRating;
    private String budget;
    private String boxOffice;
    private List<String> awards;
}
```

### 2.2 리뷰 통계
- **별점 분포**
  ```java
  public class RatingDistribution {
      private Map<Integer, Integer> distribution;  // 1-5점 분포
      private Double averageRating;
      private Integer totalReviews;
      private Integer userRating;  // 현재 사용자의 평점
  }
  ```

- **리뷰 미리보기**
  - 최신 리뷰
  - 인기 리뷰
  - 사용자 리뷰

## 3. API 엔드포인트

### 3.1 검색 관련
```
GET /api/movies/search
GET /api/movies/genres
GET /api/movies/years
GET /api/movies/directors
GET /api/movies/actors
```

### 3.2 상세 정보 관련
```
GET /api/movies/{id}
GET /api/movies/{id}/reviews
GET /api/movies/{id}/rating-distribution
GET /api/movies/{id}/similar
```

### 3.3 미디어 관련
```
GET /api/movies/{id}/posters
GET /api/movies/{id}/stills
GET /api/movies/{id}/trailers
```

## 4. 데이터베이스 스키마

### 4.1 Movie 테이블 확장
```sql
ALTER TABLE movies
ADD COLUMN original_title VARCHAR(100),
ADD COLUMN english_title VARCHAR(100),
ADD COLUMN country VARCHAR(50),
ADD COLUMN language VARCHAR(50),
ADD COLUMN running_time VARCHAR(50),
ADD COLUMN age_rating VARCHAR(50),
ADD COLUMN budget VARCHAR(100),
ADD COLUMN box_office VARCHAR(100),
ADD COLUMN release_status VARCHAR(50);
```

### 4.2 MovieMedia 테이블
```sql
CREATE TABLE movie_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    media_type VARCHAR(20) NOT NULL,  -- poster, still, trailer
    media_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
```

## 5. 검색 최적화

### 5.1 Elasticsearch 설정
- **인덱스 구조**
  ```json
  {
    "mappings": {
      "properties": {
        "title": { "type": "text", "analyzer": "korean" },
        "originalTitle": { "type": "text" },
        "englishTitle": { "type": "text" },
        "director": { "type": "keyword" },
        "actors": { "type": "keyword" },
        "genres": { "type": "keyword" },
        "releaseDate": { "type": "date" },
        "rating": { "type": "float" }
      }
    }
  }
  ```

- **검색 설정**
  - 한글 검색 지원
  - 유사도 검색
  - 필터링 최적화

### 5.2 캐싱 전략
- **캐시 키**
  ```
  movie:detail:{id}
  movie:search:{query}
  movie:genres
  movie:years
  ```

- **캐시 만료**
  - 상세 정보: 1시간
  - 검색 결과: 30분
  - 메타 데이터: 24시간

## 6. 성능 최적화

### 6.1 데이터베이스 최적화
- **인덱스 설정**
  ```sql
  CREATE INDEX idx_movies_title ON movies(title);
  CREATE INDEX idx_movies_release_date ON movies(release_date);
  CREATE INDEX idx_movies_rating ON movies(average_rating);
  CREATE INDEX idx_movie_media_type ON movie_media(media_type);
  ```

- **쿼리 최적화**
  - 조인 최적화
  - 서브쿼리 최적화
  - 페이징 처리

### 6.2 프론트엔드 최적화
- **이미지 최적화**
  - 포스터 이미지
  - 스틸컷
  - 예고편 썸네일

- **데이터 로딩**
  - 지연 로딩
  - 무한 스크롤
  - 스켈레톤 UI

## 7. 모니터링

### 7.1 검색 성능
- 검색 응답 시간
- 검색 정확도
- 필터링 성능
- 캐시 히트율

### 7.2 사용자 행동
- 검색어 통계
- 필터 사용 통계
- 상세 페이지 조회 통계
- 체류 시간 