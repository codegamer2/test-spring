export interface Review {
    id: number;
    movieId: number;
    movieTitle: string;
    userId: number;
    username: string;
    title: string;
    content: string;
    rating: number;
    createdAt: string;
    updatedAt: string;
    likeCount: number;
    commentCount: number;
    isLiked: boolean;
    likes: Like[];
    comments: Comment[];
}

export interface Like {
    id: number;
    userId: number;
    username: string;
    createdAt: string;
}

export interface Comment {
    id: number;
    userId: number;
    username: string;
    content: string;
    createdAt: string;
    updatedAt: string;
    likes: Like[];
} 