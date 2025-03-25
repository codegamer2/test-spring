export interface EmailVerificationRequest {
    email: string;
}

export interface EmailVerificationResponse {
    success: boolean;
    message: string;
}

export interface EmailVerificationConfirmRequest {
    email: string;
    token: string;
}

export interface EmailVerificationConfirmResponse {
    success: boolean;
    message: string;
} 