import { UserProfile, Review, ReviewListResponse, FollowResponse } from '../types/profile-view';
import { SocialLoginHandler } from '../auth/social-login';
import { createReviewCard } from '../components/review-card';

export class ProfileView {
    private followBtn!: HTMLButtonElement;
    private unfollowBtn!: HTMLButtonElement;
    private reviewList!: HTMLDivElement;
    private likedReviewList!: HTMLDivElement;
    private loadMoreReviewsBtn!: HTMLButtonElement;
    private loadMoreLikedReviewsBtn!: HTMLButtonElement;
    private logoutButton!: HTMLAnchorElement;
    private socialLoginHandler: SocialLoginHandler;
    private currentPage: number = 1;
    private likedReviewPage: number = 1;
    private isLoading: boolean = false;
    private isLoadingLiked: boolean = false;

    constructor() {
        this.initializeElements();
        this.initializeEventListeners();
        this.socialLoginHandler = SocialLoginHandler.getInstance();
        this.loadInitialData();
    }

    private initializeElements(): void {
        this.followBtn = document.getElementById('followBtn') as HTMLButtonElement;
        this.unfollowBtn = document.getElementById('unfollowBtn') as HTMLButtonElement;
        this.reviewList = document.getElementById('reviewList') as HTMLDivElement;
        this.likedReviewList = document.getElementById('likedReviewList') as HTMLDivElement;
        this.loadMoreReviewsBtn = document.getElementById('loadMoreReviews') as HTMLButtonElement;
        this.loadMoreLikedReviewsBtn = document.getElementById('loadMoreLikedReviews') as HTMLButtonElement;
        this.logoutButton = document.getElementById('logoutBtn') as HTMLAnchorElement;
    }

    private initializeEventListeners(): void {
        if (this.followBtn) {
            this.followBtn.addEventListener('click', this.handleFollow.bind(this));
        }
        if (this.unfollowBtn) {
            this.unfollowBtn.addEventListener('click', this.handleUnfollow.bind(this));
        }
        if (this.loadMoreReviewsBtn) {
            this.loadMoreReviewsBtn.addEventListener('click', this.loadMoreReviews.bind(this));
        }
        if (this.loadMoreLikedReviewsBtn) {
            this.loadMoreLikedReviewsBtn.addEventListener('click', this.loadMoreLikedReviews.bind(this));
        }
        this.logoutButton.addEventListener('click', this.handleLogout.bind(this));
    }

    private async loadInitialData(): Promise<void> {
        await Promise.all([
            this.loadReviews(),
            this.loadLikedReviews()
        ]);
    }

    private async loadReviews(): Promise<void> {
        if (this.isLoading) return;

        try {
            this.isLoading = true;
            this.loadMoreReviewsBtn.disabled = true;

            const response = await fetch(`/api/profile/reviews?page=${this.currentPage}`);
            if (!response.ok) {
                throw new Error('리뷰 목록을 불러오는데 실패했습니다.');
            }

            const data: ReviewListResponse = await response.json();
            this.renderReviews(data.reviews);
            this.updateLoadMoreButton(this.loadMoreReviewsBtn, data.hasMore);
        } catch (error) {
            console.error('리뷰 목록 로딩 중 오류 발생:', error);
            this.showErrorMessage('리뷰 목록을 불러오는데 실패했습니다.');
        } finally {
            this.isLoading = false;
            this.loadMoreReviewsBtn.disabled = false;
        }
    }

    private async loadMoreReviews(): Promise<void> {
        this.currentPage++;
        await this.loadReviews();
    }

    private async loadLikedReviews(): Promise<void> {
        if (this.isLoadingLiked) return;

        try {
            this.isLoadingLiked = true;
            this.loadMoreLikedReviewsBtn.disabled = true;

            const response = await fetch(`/api/profile/liked-reviews?page=${this.likedReviewPage}`);
            if (!response.ok) {
                throw new Error('좋아요한 리뷰 목록을 불러오는데 실패했습니다.');
            }

            const data: ReviewListResponse = await response.json();
            this.renderLikedReviews(data.reviews);
            this.updateLoadMoreButton(this.loadMoreLikedReviewsBtn, data.hasMore);
        } catch (error) {
            console.error('좋아요한 리뷰 목록 로딩 중 오류 발생:', error);
            this.showErrorMessage('좋아요한 리뷰 목록을 불러오는데 실패했습니다.');
        } finally {
            this.isLoadingLiked = false;
            this.loadMoreLikedReviewsBtn.disabled = false;
        }
    }

    private async loadMoreLikedReviews(): Promise<void> {
        this.likedReviewPage++;
        await this.loadLikedReviews();
    }

    private renderReviews(reviews: Review[]): void {
        reviews.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-item';
            reviewElement.innerHTML = createReviewCard(review);
            this.reviewList.appendChild(reviewElement);
        });
    }

    private renderLikedReviews(reviews: Review[]): void {
        reviews.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-item';
            reviewElement.innerHTML = createReviewCard(review);
            this.likedReviewList.appendChild(reviewElement);
        });
    }

    private updateLoadMoreButton(button: HTMLButtonElement, hasMore: boolean): void {
        button.style.display = hasMore ? 'inline-block' : 'none';
    }

    private async handleFollow(): Promise<void> {
        try {
            const response = await fetch('/api/profile/follow', {
                method: 'POST',
            });

            if (!response.ok) {
                throw new Error('팔로우에 실패했습니다.');
            }

            const data: FollowResponse = await response.json();
            this.updateFollowStatus(data);
            this.showSuccessMessage('팔로우되었습니다.');
        } catch (error) {
            console.error('팔로우 중 오류 발생:', error);
            this.showErrorMessage('팔로우에 실패했습니다.');
        }
    }

    private async handleUnfollow(): Promise<void> {
        try {
            const response = await fetch('/api/profile/unfollow', {
                method: 'POST',
            });

            if (!response.ok) {
                throw new Error('언팔로우에 실패했습니다.');
            }

            const data: FollowResponse = await response.json();
            this.updateFollowStatus(data);
            this.showSuccessMessage('언팔로우되었습니다.');
        } catch (error) {
            console.error('언팔로우 중 오류 발생:', error);
            this.showErrorMessage('언팔로우에 실패했습니다.');
        }
    }

    private updateFollowStatus(data: FollowResponse): void {
        document.getElementById('followerCount')!.textContent = data.followerCount.toString();
        this.followBtn.style.display = data.isFollowing ? 'none' : 'inline-block';
        this.unfollowBtn.style.display = data.isFollowing ? 'inline-block' : 'none';
    }

    private handleLogout(event: Event): void {
        event.preventDefault();
        this.socialLoginHandler.logout();
    }

    private showSuccessMessage(message: string): void {
        // TODO: 토스트 메시지 구현
        alert(message);
    }

    private showErrorMessage(message: string): void {
        // TODO: 토스트 메시지 구현
        alert(message);
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new ProfileView();
}); 