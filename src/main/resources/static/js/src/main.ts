import { Movie, MovieResponse, Review, ReviewResponse } from './types/main';
import { createMovieCard, createSkeletonMovieCard } from './components/movie-card';
import { createReviewCard, createSkeletonReviewCard } from './components/review-card';
import { SocialLoginHandler } from './auth/social-login';

export class MainPage {
    private recommendedMoviesContainer!: HTMLDivElement;
    private popularReviewsContainer!: HTMLDivElement;
    private followingReviewsContainer!: HTMLDivElement;
    private newMoviesByGenreContainer!: HTMLDivElement;
    private searchForm!: HTMLFormElement;
    private searchInput!: HTMLInputElement;
    private logoutButton!: HTMLAnchorElement;
    private socialLoginHandler: SocialLoginHandler;
    private currentRecommendedPage: number = 1;
    private currentPopularPage: number = 1;
    private currentFollowingPage: number = 1;
    private currentNewMoviesPage: number = 1;
    private isLoadingRecommended: boolean = false;
    private isLoadingPopular: boolean = false;
    private isLoadingFollowing: boolean = false;
    private isLoadingNewMovies: boolean = false;

    constructor() {
        this.initializeElements();
        this.initializeEventListeners();
        this.socialLoginHandler = SocialLoginHandler.getInstance();
        this.loadInitialData();
    }

    private initializeElements(): void {
        this.recommendedMoviesContainer = document.getElementById('recommendedMovies') as HTMLDivElement;
        this.popularReviewsContainer = document.getElementById('popularReviews') as HTMLDivElement;
        this.followingReviewsContainer = document.getElementById('followingReviews') as HTMLDivElement;
        this.newMoviesByGenreContainer = document.getElementById('newMoviesByGenre') as HTMLDivElement;
        this.searchForm = document.getElementById('searchForm') as HTMLFormElement;
        this.searchInput = document.getElementById('searchInput') as HTMLInputElement;
        this.logoutButton = document.getElementById('logoutBtn') as HTMLAnchorElement;
    }

    private initializeEventListeners(): void {
        this.searchForm.addEventListener('submit', this.handleSearch.bind(this));
        this.logoutButton.addEventListener('click', this.handleLogout.bind(this));

        // 무한 스크롤 이벤트 리스너
        window.addEventListener('scroll', this.handleScroll.bind(this));
    }

    private async loadInitialData(): Promise<void> {
        await Promise.all([
            this.loadRecommendedMovies(),
            this.loadPopularReviews()
        ]);

        // 로그인된 사용자만 추가 데이터 로드
        if (this.isAuthenticated()) {
            await Promise.all([
                this.loadFollowingReviews(),
                this.loadNewMoviesByGenre()
            ]);
        }
    }

    private async loadRecommendedMovies(): Promise<void> {
        if (this.isLoadingRecommended) return;

        try {
            this.isLoadingRecommended = true;
            this.showSkeletonLoading(this.recommendedMoviesContainer, createSkeletonMovieCard);

            const response = await fetch(`/api/movies/recommended?page=${this.currentRecommendedPage}`);
            if (!response.ok) {
                throw new Error('추천 영화를 불러오는데 실패했습니다.');
            }

            const data: MovieResponse = await response.json();
            this.renderMovies(this.recommendedMoviesContainer, data.movies);
            this.currentRecommendedPage = data.nextPage;
        } catch (error) {
            console.error('추천 영화 로딩 중 오류 발생:', error);
            this.showErrorMessage('추천 영화를 불러오는데 실패했습니다.');
        } finally {
            this.isLoadingRecommended = false;
        }
    }

    private async loadPopularReviews(): Promise<void> {
        if (this.isLoadingPopular) return;

        try {
            this.isLoadingPopular = true;
            this.showSkeletonLoading(this.popularReviewsContainer, createSkeletonReviewCard);

            const response = await fetch(`/api/reviews/popular?page=${this.currentPopularPage}`);
            if (!response.ok) {
                throw new Error('인기 리뷰를 불러오는데 실패했습니다.');
            }

            const data: ReviewResponse = await response.json();
            this.renderReviews(this.popularReviewsContainer, data.reviews);
            this.currentPopularPage = data.nextPage;
        } catch (error) {
            console.error('인기 리뷰 로딩 중 오류 발생:', error);
            this.showErrorMessage('인기 리뷰를 불러오는데 실패했습니다.');
        } finally {
            this.isLoadingPopular = false;
        }
    }

    private async loadFollowingReviews(): Promise<void> {
        if (this.isLoadingFollowing) return;

        try {
            this.isLoadingFollowing = true;
            this.showSkeletonLoading(this.followingReviewsContainer, createSkeletonReviewCard);

            const response = await fetch(`/api/reviews/following?page=${this.currentFollowingPage}`);
            if (!response.ok) {
                throw new Error('팔로우 중인 사용자의 리뷰를 불러오는데 실패했습니다.');
            }

            const data: ReviewResponse = await response.json();
            this.renderReviews(this.followingReviewsContainer, data.reviews);
            this.currentFollowingPage = data.nextPage;
        } catch (error) {
            console.error('팔로우 중인 사용자의 리뷰 로딩 중 오류 발생:', error);
            this.showErrorMessage('팔로우 중인 사용자의 리뷰를 불러오는데 실패했습니다.');
        } finally {
            this.isLoadingFollowing = false;
        }
    }

    private async loadNewMoviesByGenre(): Promise<void> {
        if (this.isLoadingNewMovies) return;

        try {
            this.isLoadingNewMovies = true;
            this.showSkeletonLoading(this.newMoviesByGenreContainer, createSkeletonMovieCard);

            const response = await fetch(`/api/movies/new-by-genre?page=${this.currentNewMoviesPage}`);
            if (!response.ok) {
                throw new Error('관심 장르의 새로운 영화를 불러오는데 실패했습니다.');
            }

            const data: MovieResponse = await response.json();
            this.renderMovies(this.newMoviesByGenreContainer, data.movies);
            this.currentNewMoviesPage = data.nextPage;
        } catch (error) {
            console.error('관심 장르의 새로운 영화 로딩 중 오류 발생:', error);
            this.showErrorMessage('관심 장르의 새로운 영화를 불러오는데 실패했습니다.');
        } finally {
            this.isLoadingNewMovies = false;
        }
    }

    private renderMovies(container: HTMLDivElement, movies: Movie[]): void {
        container.innerHTML = movies.map(movie => createMovieCard(movie)).join('');
    }

    private renderReviews(container: HTMLDivElement, reviews: Review[]): void {
        container.innerHTML = reviews.map(review => createReviewCard(review)).join('');
    }

    private showSkeletonLoading(container: HTMLDivElement, createSkeleton: () => string): void {
        container.innerHTML = Array(4).fill(null).map(() => createSkeleton()).join('');
    }

    private handleSearch(event: Event): void {
        event.preventDefault();
        const query = this.searchInput.value.trim();
        if (query) {
            window.location.href = `/movies/search?q=${encodeURIComponent(query)}`;
        }
    }

    private handleLogout(event: Event): void {
        event.preventDefault();
        this.socialLoginHandler.logout();
    }

    private handleScroll(): void {
        const scrollPosition = window.innerHeight + window.scrollY;
        const documentHeight = document.documentElement.scrollHeight;
        const threshold = 200;

        if (scrollPosition >= documentHeight - threshold) {
            if (this.isAuthenticated()) {
                this.loadMoreContent();
            } else {
                this.loadMoreContentForGuest();
            }
        }
    }

    private async loadMoreContent(): Promise<void> {
        await Promise.all([
            this.loadRecommendedMovies(),
            this.loadPopularReviews(),
            this.loadFollowingReviews(),
            this.loadNewMoviesByGenre()
        ]);
    }

    private async loadMoreContentForGuest(): Promise<void> {
        await Promise.all([
            this.loadRecommendedMovies(),
            this.loadPopularReviews()
        ]);
    }

    private isAuthenticated(): boolean {
        return document.body.classList.contains('authenticated');
    }

    private showErrorMessage(message: string): void {
        // TODO: 토스트 메시지 구현
        alert(message);
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new MainPage();
}); 