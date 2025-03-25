import { ReviewRequest } from '../types/movie-detail';

export class ReviewEdit {
    private reviewId: number;
    private modal!: HTMLElement;
    private form!: HTMLFormElement;
    private ratingInput!: HTMLElement;
    private ratingStars!: NodeListOf<HTMLElement>;
    private currentRating: number = 0;
    private originalReview: ReviewRequest;

    constructor(reviewId: number, originalReview: ReviewRequest) {
        this.reviewId = reviewId;
        this.originalReview = originalReview;
        this.initializeElements();
        this.initializeEventListeners();
        this.setOriginalValues();
    }

    private initializeElements(): void {
        this.modal = document.getElementById('editReviewModal') as HTMLElement;
        this.form = document.getElementById('editReviewForm') as HTMLFormElement;
        this.ratingInput = document.querySelector('.edit-rating-input') as HTMLElement;
        this.ratingStars = this.ratingInput.querySelectorAll('i');
    }

    private initializeEventListeners(): void {
        // 별점 입력 이벤트
        this.ratingStars.forEach(star => {
            star.addEventListener('mouseover', () => this.handleStarHover(star));
            star.addEventListener('mouseout', () => this.handleStarOut());
            star.addEventListener('click', () => this.handleStarClick(star));
        });

        // 폼 제출 이벤트
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));

        // 삭제 버튼 이벤트
        const deleteButton = document.getElementById('deleteReviewButton') as HTMLButtonElement;
        deleteButton.addEventListener('click', () => this.handleDelete());
    }

    private setOriginalValues(): void {
        (document.getElementById('editReviewTitle') as HTMLInputElement).value = this.originalReview.title;
        (document.getElementById('editReviewContent') as HTMLTextAreaElement).value = this.originalReview.content;
        (document.getElementById('editReviewRating') as HTMLInputElement).value = this.originalReview.rating.toString();
        (document.getElementById('editReviewSpoiler') as HTMLInputElement).checked = this.originalReview.hasSpoiler;
        
        this.currentRating = this.originalReview.rating;
        this.updateStars(this.currentRating);
    }

    private handleStarHover(star: HTMLElement): void {
        const rating = parseInt(star.getAttribute('data-rating') || '0');
        this.updateStars(rating);
    }

    private handleStarOut(): void {
        this.updateStars(this.currentRating);
    }

    private handleStarClick(star: HTMLElement): void {
        this.currentRating = parseInt(star.getAttribute('data-rating') || '0');
        this.updateStars(this.currentRating);
        (document.getElementById('editReviewRating') as HTMLInputElement).value = this.currentRating.toString();
    }

    private updateStars(rating: number): void {
        this.ratingStars.forEach(star => {
            const starRating = parseInt(star.getAttribute('data-rating') || '0');
            if (starRating <= rating) {
                star.classList.add('bi-star-fill');
                star.classList.remove('bi-star');
            } else {
                star.classList.remove('bi-star-fill');
                star.classList.add('bi-star');
            }
        });
    }

    private async handleSubmit(e: Event): Promise<void> {
        e.preventDefault();
        
        const formData = new FormData(this.form);
        const review: ReviewRequest = {
            title: formData.get('title') as string,
            content: formData.get('content') as string,
            rating: Number(formData.get('rating')),
            hasSpoiler: formData.get('hasSpoiler') === 'true'
        };

        try {
            const response = await fetch(`/api/reviews/${this.reviewId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(review)
            });

            if (!response.ok) throw new Error('리뷰 수정에 실패했습니다.');

            const data = await response.json();
            if (data.success) {
                this.showSuccess('리뷰가 수정되었습니다.');
                this.hide();
                
                // 리뷰 수정 이벤트 발생
                const updateEvent = new CustomEvent('reviewUpdate', {
                    detail: data.review
                });
                document.dispatchEvent(updateEvent);
            }
        } catch (error) {
            console.error('리뷰 수정 실패:', error);
            this.showError('리뷰 수정에 실패했습니다.');
        }
    }

    private async handleDelete(): Promise<void> {
        if (!confirm('정말로 이 리뷰를 삭제하시겠습니까?')) return;

        try {
            const response = await fetch(`/api/reviews/${this.reviewId}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error('리뷰 삭제에 실패했습니다.');

            const data = await response.json();
            if (data.success) {
                this.showSuccess('리뷰가 삭제되었습니다.');
                this.hide();
                
                // 리뷰 삭제 이벤트 발생
                const deleteEvent = new CustomEvent('reviewDelete', {
                    detail: { reviewId: this.reviewId }
                });
                document.dispatchEvent(deleteEvent);
            }
        } catch (error) {
            console.error('리뷰 삭제 실패:', error);
            this.showError('리뷰 삭제에 실패했습니다.');
        }
    }

    public show(): void {
        const modal = new (window as any).bootstrap.Modal(this.modal);
        modal.show();
    }

    public hide(): void {
        const modal = (window as any).bootstrap.Modal.getInstance(this.modal);
        if (modal) {
            modal.hide();
        }
    }

    private showSuccess(message: string): void {
        // 성공 메시지 표시 로직 구현
    }

    private showError(message: string): void {
        // 에러 메시지 표시 로직 구현
    }
} 