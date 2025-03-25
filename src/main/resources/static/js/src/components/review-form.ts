import { ReviewRequest } from '../types/movie-detail';

export class ReviewForm {
    private form: HTMLFormElement;
    private modal: HTMLElement;
    private ratingInput: HTMLElement;
    private ratingStars: NodeListOf<HTMLElement>;
    private currentRating: number = 0;

    constructor() {
        this.form = document.getElementById('reviewForm') as HTMLFormElement;
        this.modal = document.getElementById('writeReviewModal') as HTMLElement;
        this.ratingInput = document.querySelector('.rating-input') as HTMLElement;
        this.ratingStars = this.ratingInput.querySelectorAll('i');
        
        this.initializeEventListeners();
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
        (document.getElementById('reviewRating') as HTMLInputElement).value = this.currentRating.toString();
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

    private handleSubmit(e: Event): void {
        e.preventDefault();
        
        const formData = new FormData(this.form);
        const review: ReviewRequest = {
            title: formData.get('title') as string,
            content: formData.get('content') as string,
            rating: Number(formData.get('rating')),
            hasSpoiler: formData.get('hasSpoiler') === 'true'
        };

        // 커스텀 이벤트 발생
        const submitEvent = new CustomEvent('reviewSubmit', {
            detail: review
        });
        this.form.dispatchEvent(submitEvent);
    }

    public show(): void {
        const modal = new bootstrap.Modal(this.modal);
        modal.show();
    }

    public hide(): void {
        const modal = bootstrap.Modal.getInstance(this.modal);
        if (modal) {
            modal.hide();
        }
    }

    public reset(): void {
        this.form.reset();
        this.currentRating = 0;
        this.updateStars(0);
    }
} 