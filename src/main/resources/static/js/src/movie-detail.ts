import { MovieDetail, Review, ReviewResponse, ReviewRequest, WatchlistResponse } from './types/movie-detail';

export class MovieDetailPage {
    private movieId: number;
    private currentPage: number = 1;
    private isLoading: boolean = false;
    private hasMore: boolean = true;

    // DOM Elements
    private poster!: HTMLImageElement;
    private title!: HTMLElement;
    private info!: HTMLElement;
    private description!: HTMLElement;
    private trailerButton!: HTMLButtonElement;
    private watchlistButton!: HTMLButtonElement;
    private reviewForm!: HTMLFormElement;
    private reviewList!: HTMLElement;
    private loadMoreButton!: HTMLButtonElement;

    constructor(movieId: number) {
        this.movieId = movieId;
        this.initializeElements();
        this.attachEventListeners();
        this.loadMovieDetail();
        this.loadReviews();
    }

    private initializeElements(): void {
        this.poster = document.getElementById('movie-poster') as HTMLImageElement;
        this.title = document.getElementById('movie-title') as HTMLElement;
        this.info = document.getElementById('movie-info') as HTMLElement;
        this.description = document.getElementById('movie-description') as HTMLElement;
        this.trailerButton = document.getElementById('trailer-button') as HTMLButtonElement;
        this.watchlistButton = document.getElementById('watchlist-button') as HTMLButtonElement;
        this.reviewForm = document.getElementById('review-form') as HTMLFormElement;
        this.reviewList = document.getElementById('review-list') as HTMLElement;
        this.loadMoreButton = document.getElementById('load-more') as HTMLButtonElement;
    }

    private attachEventListeners(): void {
        this.trailerButton.addEventListener('click', () => this.showTrailer());
        this.watchlistButton.addEventListener('click', () => this.toggleWatchlist());
        this.reviewForm.addEventListener('submit', (e) => this.handleReviewSubmit(e));
        this.loadMoreButton.addEventListener('click', () => this.loadMoreReviews());
    }

    private async loadMovieDetail(): Promise<void> {
        try {
            const response = await fetch(`/api/movies/${this.movieId}`);
            if (!response.ok) throw new Error('영화 정보를 불러오는데 실패했습니다.');
            
            const movie: MovieDetail = await response.json();
            this.renderMovieDetail(movie);
            this.checkWatchlistStatus();
        } catch (error) {
            console.error('영화 정보 로딩 실패:', error);
            this.showError('영화 정보를 불러오는데 실패했습니다.');
        }
    }

    private renderMovieDetail(movie: MovieDetail): void {
        this.poster.src = movie.posterPath;
        this.title.textContent = movie.title;
        this.info.innerHTML = `
            <p class="mb-2">개봉일: ${movie.releaseDate}</p>
            <p class="mb-2">감독: ${movie.director}</p>
            <p class="mb-2">출연: ${movie.actors.join(', ')}</p>
            <p class="mb-2">장르: ${movie.genres.join(', ')}</p>
            <div class="rating">
                <i class="bi bi-star-fill text-warning"></i>
                <span>${movie.rating.toFixed(1)}</span>
            </div>
        `;
        this.description.textContent = movie.description;
    }

    private async loadReviews(): Promise<void> {
        if (this.isLoading || !this.hasMore) return;
        
        try {
            this.isLoading = true;
            this.loadMoreButton.disabled = true;
            
            const response = await fetch(`/api/movies/${this.movieId}/reviews?page=${this.currentPage}`);
            if (!response.ok) throw new Error('리뷰를 불러오는데 실패했습니다.');
            
            const data: ReviewResponse = await response.json();
            this.renderReviews(data.reviews);
            this.hasMore = data.hasMore;
            this.currentPage = data.nextPage;
            
            this.loadMoreButton.style.display = this.hasMore ? 'block' : 'none';
        } catch (error) {
            console.error('리뷰 로딩 실패:', error);
            this.showError('리뷰를 불러오는데 실패했습니다.');
        } finally {
            this.isLoading = false;
            this.loadMoreButton.disabled = false;
        }
    }

    private renderReviews(reviews: Review[]): void {
        const fragment = document.createDocumentFragment();
        
        reviews.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-card';
            reviewElement.innerHTML = `
                <div class="review-header">
                    <div class="d-flex align-items-center">
                        <img src="${review.profileImage || '/images/default-profile.png'}" 
                             alt="${review.username}" 
                             class="rounded-circle me-2"
                             style="width: 40px; height: 40px;">
                        <div>
                            <h5 class="mb-0">${review.username}</h5>
                            <small class="text-muted">${new Date(review.createdAt).toLocaleDateString()}</small>
                        </div>
                    </div>
                    <div class="rating">
                        <i class="bi bi-star-fill text-warning"></i>
                        <span>${review.rating}</span>
                    </div>
                </div>
                <div class="review-content">
                    <h6>${review.title}</h6>
                    <p>${review.content}</p>
                    ${review.hasSpoiler ? '<span class="badge bg-danger">스포일러</span>' : ''}
                </div>
                <div class="review-footer">
                    <button class="btn btn-link like-button ${review.isLiked ? 'text-danger' : ''}"
                            data-review-id="${review.id}">
                        <i class="bi ${review.isLiked ? 'bi-heart-fill' : 'bi-heart'}"></i>
                        <span>${review.likeCount}</span>
                    </button>
                    <button class="btn btn-link comment-button"
                            data-review-id="${review.id}">
                        <i class="bi bi-chat"></i>
                        <span>${review.commentCount}</span>
                    </button>
                </div>
            `;
            
            fragment.appendChild(reviewElement);
        });
        
        this.reviewList.appendChild(fragment);
    }

    private async handleReviewSubmit(e: Event): Promise<void> {
        e.preventDefault();
        
        const formData = new FormData(this.reviewForm);
        const review: ReviewRequest = {
            title: formData.get('title') as string,
            content: formData.get('content') as string,
            rating: Number(formData.get('rating')),
            hasSpoiler: formData.get('hasSpoiler') === 'true'
        };
        
        try {
            const response = await fetch(`/api/movies/${this.movieId}/reviews`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(review)
            });
            
            if (!response.ok) throw new Error('리뷰 작성에 실패했습니다.');
            
            const data: ReviewResponse = await response.json();
            if (data.success) {
                this.showSuccess('리뷰가 등록되었습니다.');
                this.reviewForm.reset();
                this.currentPage = 1;
                this.hasMore = true;
                this.reviewList.innerHTML = '';
                this.loadReviews();
            }
        } catch (error) {
            console.error('리뷰 작성 실패:', error);
            this.showError('리뷰 작성에 실패했습니다.');
        }
    }

    private async toggleWatchlist(): Promise<void> {
        try {
            const response = await fetch(`/api/movies/${this.movieId}/watchlist`, {
                method: 'POST'
            });
            
            if (!response.ok) throw new Error('위시리스트 상태 변경에 실패했습니다.');
            
            const data: WatchlistResponse = await response.json();
            if (data.success) {
                this.watchlistButton.innerHTML = data.isInWatchlist
                    ? '<i class="bi bi-heart-fill"></i> 위시리스트에서 제거'
                    : '<i class="bi bi-heart"></i> 위시리스트에 추가';
                this.showSuccess(data.message);
            }
        } catch (error) {
            console.error('위시리스트 상태 변경 실패:', error);
            this.showError('위시리스트 상태 변경에 실패했습니다.');
        }
    }

    private async checkWatchlistStatus(): Promise<void> {
        try {
            const response = await fetch(`/api/movies/${this.movieId}/watchlist/status`);
            if (!response.ok) throw new Error('위시리스트 상태 확인에 실패했습니다.');
            
            const data: WatchlistResponse = await response.json();
            if (data.success) {
                this.watchlistButton.innerHTML = data.isInWatchlist
                    ? '<i class="bi bi-heart-fill"></i> 위시리스트에서 제거'
                    : '<i class="bi bi-heart"></i> 위시리스트에 추가';
            }
        } catch (error) {
            console.error('위시리스트 상태 확인 실패:', error);
        }
    }

    private async loadMoreReviews(): Promise<void> {
        await this.loadReviews();
    }

    private showTrailer(): void {
        // 트레일러 모달 표시 로직 구현
    }

    private showSuccess(message: string): void {
        // 성공 메시지 표시 로직 구현
    }

    private showError(message: string): void {
        // 에러 메시지 표시 로직 구현
    }
} 