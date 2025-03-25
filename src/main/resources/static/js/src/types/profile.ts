export interface ProfileUpdateRequest {
    nickname: string;
    bio?: string;
    genres: string[];
    password?: string;
}

export interface ProfileUpdateResponse {
    success: boolean;
    message: string;
    user: {
        id: number;
        email: string;
        nickname: string;
        bio?: string;
        profileImage?: string;
        genres: string[];
    };
}

export interface ProfileError {
    field?: string;
    message: string;
}

export interface ProfileImageUploadResponse {
    success: boolean;
    message: string;
    imageUrl: string;
} 