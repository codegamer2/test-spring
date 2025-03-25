export interface LoginRequest {
    email: string;
    password: string;
    rememberMe: boolean;
}

export interface LoginResponse {
    success: boolean;
    message: string;
    token?: string;
    user?: {
        id: number;
        email: string;
        nickname: string;
        profileImage?: string;
    };
}

export interface AuthError {
    field?: string;
    message: string;
}

export interface SocialLoginResponse {
    token: string;
    user: {
        id: number;
        email: string;
        name: string;
        profileImage: string;
        provider: string;
    };
}

export interface SocialLoginError {
    message: string;
    code: string;
}

export type SocialProvider = 'google' | 'naver' | 'kakao'; 