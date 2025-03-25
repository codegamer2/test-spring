import { SocialLoginResponse, SocialLoginError, SocialProvider } from '../types/auth';

export class SocialLoginHandler {
    private static instance: SocialLoginHandler;
    private tokenKey = 'auth_token';

    private constructor() {
        this.handleCallback();
    }

    public static getInstance(): SocialLoginHandler {
        if (!SocialLoginHandler.instance) {
            SocialLoginHandler.instance = new SocialLoginHandler();
        }
        return SocialLoginHandler.instance;
    }

    private handleCallback(): void {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');

        if (token) {
            this.handleToken(token);
        }
    }

    private handleToken(token: string): void {
        localStorage.setItem(this.tokenKey, token);
        window.location.href = '/';
    }

    public async handleSocialLogin(provider: SocialProvider): Promise<void> {
        try {
            const response = await fetch(`/api/auth/${provider}/callback`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ provider }),
            });

            if (!response.ok) {
                const error: SocialLoginError = await response.json();
                throw new Error(error.message);
            }

            const data: SocialLoginResponse = await response.json();
            this.handleToken(data.token);
        } catch (error) {
            console.error('소셜 로그인 중 오류 발생:', error);
            alert('소셜 로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
    }

    public getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    public logout(): void {
        localStorage.removeItem(this.tokenKey);
        window.location.href = '/login';
    }
} 