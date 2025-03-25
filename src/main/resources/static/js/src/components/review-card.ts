import { Review } from '../types/main';

export function createReviewCard(review: Review): string {
    return `
        <div class="col-md-6 mb-4">
            <div class="card review-card">
                <div class="card-body">
                    <div class="review-header">
                        <img src="${review.profileImage || '/images/default-profile.png'}" 
                             class="profile-image" 
                             alt="${review.username}">
                        <div class="review-info">
                            <div class="username">${review.username}</div>
                            <div class="movie-title">${review.movieTitle}</div>
                        </div>
                    </div>
                    <h5 class="card-title">${review.title}</h5>
                    <p class="review-content">${review.content}</p>
                    <div class="review-footer">
                        <div class="review-stats">
                            <span>
                                <i class="bi bi-heart${review.isLiked ? '-fill' : ''}"></i>
                                ${review.likeCount}
                            </span>
                            <span>
                                <i class="bi bi-chat"></i>
                                ${review.commentCount}
                            </span>
                        </div>
                        <small class="text-muted">
                            ${new Date(review.createdAt).toLocaleDateString()}
                        </small>
                    </div>
                </div>
            </div>
        </div>
    `;
}

export function createSkeletonReviewCard(): string {
    return `
        <div class="col-md-6 mb-4">
            <div class="card review-card">
                <div class="card-body">
                    <div class="skeleton skeleton-review-card"></div>
                </div>
            </div>
        </div>
    `;
} 