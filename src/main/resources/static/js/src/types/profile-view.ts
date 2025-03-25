import { Review } from './review';

export interface UserProfile {
    id: number;
    email: string;
    nickname: string;
    bio?: string;
    profileImage?: string;
    genres: string[];
    followerCount: number;
    followingCount: number;
    reviewCount: number;
    isFollowing: boolean;
}

export interface ReviewListResponse {
    reviews: Review[];
    hasMore: boolean;
    nextPage: number;
}

export interface FollowResponse {
    success: boolean;
    message: string;
    followerCount: number;
    isFollowing: boolean;
} 