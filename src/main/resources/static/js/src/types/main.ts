export interface Movie {
    id: number;
    title: string;
    posterPath: string;
    releaseDate: string;
    rating: number;
    genres: string[];
}

export interface MovieResponse {
    movies: Movie[];
    hasMore: boolean;
    nextPage: number;
}

export interface Review {
    id: number;
    movieId: number;
    movieTitle: string;
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
}

export interface ReviewResponse {
    reviews: Review[];
    hasMore: boolean;
    nextPage: number;
} 