import { Review } from '../types/movie-detail';

export class ReviewInteraction {
    private reviewId: number;
    private likeButton!: HTMLButtonElement;
    private commentButton!: HTMLButtonElement;
    private commentSection!: HTMLElement;
    private commentForm!: HTMLFormElement;
    private commentList!: HTMLElement;
    private currentUserId: number;

    constructor(reviewId: number, currentUserId: number) {
        this.reviewId = reviewId;
        this.currentUserId = currentUserId;
        this.initializeElements();
        this.attachEventListeners();
    }

    private initializeElements(): void {
        this.likeButton = document.querySelector(`.like-button[data-review-id="${this.reviewId}"]`) as HTMLButtonElement;
        this.commentButton = document.querySelector(`.comment-button[data-review-id="${this.reviewId}"]`) as HTMLButtonElement;
        this.commentSection = document.querySelector(`#comment-section-${this.reviewId}`) as HTMLElement;
        this.commentForm = document.querySelector(`#comment-form-${this.reviewId}`) as HTMLFormElement;
        this.commentList = document.querySelector(`#comment-list-${this.reviewId}`) as HTMLElement;
    }

    private attachEventListeners(): void {
        this.likeButton.addEventListener('click', () => this.handleLike());
        this.commentButton.addEventListener('click', () => this.toggleCommentSection());
        this.commentForm.addEventListener('submit', (e) => this.handleCommentSubmit(e));
    }

    private async handleLike(): Promise<void> {
        try {
            const response = await fetch(`/api/reviews/${this.reviewId}/like`, {
                method: 'POST'
            });

            if (!response.ok) throw new Error('좋아요 처리에 실패했습니다.');

            const data = await response.json();
            if (data.success) {
                const likeCount = this.likeButton.querySelector('span');
                if (likeCount) {
                    likeCount.textContent = data.likeCount.toString();
                }
                this.likeButton.classList.toggle('text-danger');
                this.likeButton.querySelector('i')?.classList.toggle('bi-heart-fill');
                this.likeButton.querySelector('i')?.classList.toggle('bi-heart');
            }
        } catch (error) {
            console.error('좋아요 처리 실패:', error);
            this.showError('좋아요 처리에 실패했습니다.');
        }
    }

    private toggleCommentSection(): void {
        if (this.commentSection.style.display === 'none' || !this.commentSection.style.display) {
            this.commentSection.style.display = 'block';
            this.loadComments();
        } else {
            this.commentSection.style.display = 'none';
        }
    }

    private async loadComments(): Promise<void> {
        try {
            const response = await fetch(`/api/reviews/${this.reviewId}/comments`);
            if (!response.ok) throw new Error('댓글을 불러오는데 실패했습니다.');

            const data = await response.json();
            this.renderComments(data.comments);
        } catch (error) {
            console.error('댓글 로딩 실패:', error);
            this.showError('댓글을 불러오는데 실패했습니다.');
        }
    }

    private renderComments(comments: any[]): void {
        this.commentList.innerHTML = '';
        
        comments.forEach(comment => {
            const commentElement = document.createElement('div');
            commentElement.className = 'comment-item';
            commentElement.innerHTML = `
                <div class="d-flex align-items-center mb-2">
                    <img src="${comment.userProfileImage || '/images/default-profile.png'}" 
                         alt="${comment.username}" 
                         class="rounded-circle me-2"
                         style="width: 32px; height: 32px;">
                    <div>
                        <h6 class="mb-0">${comment.username}</h6>
                        <small class="text-muted">${new Date(comment.createdAt).toLocaleDateString()}</small>
                    </div>
                </div>
                <p class="mb-2">${comment.content}</p>
                <div class="comment-actions">
                    <button class="btn btn-link btn-sm reply-button" data-comment-id="${comment.id}">
                        답글 달기
                    </button>
                    ${comment.userId === this.currentUserId ? `
                        <button class="btn btn-link btn-sm text-danger delete-button" data-comment-id="${comment.id}">
                            삭제
                        </button>
                    ` : ''}
                </div>
            `;
            
            this.commentList.appendChild(commentElement);
        });
    }

    private async handleCommentSubmit(e: Event): Promise<void> {
        e.preventDefault();
        
        const formData = new FormData(this.commentForm);
        const content = formData.get('content') as string;
        
        try {
            const response = await fetch(`/api/reviews/${this.reviewId}/comments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ content })
            });

            if (!response.ok) throw new Error('댓글 작성에 실패했습니다.');

            const data = await response.json();
            if (data.success) {
                this.showSuccess('댓글이 등록되었습니다.');
                this.commentForm.reset();
                this.loadComments();
                this.updateCommentCount();
            }
        } catch (error) {
            console.error('댓글 작성 실패:', error);
            this.showError('댓글 작성에 실패했습니다.');
        }
    }

    private updateCommentCount(): void {
        const commentCount = this.commentButton.querySelector('span');
        if (commentCount) {
            const currentCount = parseInt(commentCount.textContent || '0');
            commentCount.textContent = (currentCount + 1).toString();
        }
    }

    private showSuccess(message: string): void {
        // 성공 메시지 표시 로직 구현
    }

    private showError(message: string): void {
        // 에러 메시지 표시 로직 구현
    }
} 