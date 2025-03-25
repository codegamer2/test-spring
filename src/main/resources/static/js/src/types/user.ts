export interface RegisterRequest {
    email: string;
    password: string;
    nickname: string;
    genres: string[];
}

export interface RegisterResponse {
    success: boolean;
    message: string;
    userId?: number;
}

export interface ValidationError {
    field: string;
    message: string;
} 