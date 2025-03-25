export interface MovieDetail {
    id: number;
    title: string;
    posterPath: string;
    releaseDate: string;
    rating: number;
    genres: string[];
    director: string;
    actors: string[];
    description: string;
    trailerUrl: string;
}

export interface Review {
    id: number;
    userId: number;
    username: string;
    profileImage?: string;
    title: string;
    content: string;
    rating: number;
    createdAt: string;
    likeCount: number;
    commentCount: number;
    isLiked: boolean;
    hasSpoiler: boolean;
}

export interface ReviewResponse {
    reviews: Review[];
    hasMore: boolean;
    nextPage: number;
}

export interface ReviewRequest {
    title: string;
    content: string;
    rating: number;
    hasSpoiler: boolean;
}

export interface ReviewResponse {
    success: boolean;
    message: string;
    review?: Review;
}

export interface WatchlistResponse {
    success: boolean;
    message: string;
    isInWatchlist: boolean;
} 