import { LoginRequest, LoginResponse, AuthError } from './types/auth';

class LoginForm {
    private form: HTMLFormElement;
    private emailInput: HTMLInputElement;
    private passwordInput: HTMLInputElement;
    private rememberMeCheckbox: HTMLInputElement;
    private socialLoginButton: HTMLButtonElement;

    constructor() {
        this.form = document.getElementById('loginForm') as HTMLFormElement;
        this.emailInput = document.getElementById('email') as HTMLInputElement;
        this.passwordInput = document.getElementById('password') as HTMLInputElement;
        this.rememberMeCheckbox = document.getElementById('rememberMe') as HTMLInputElement;
        this.socialLoginButton = document.getElementById('socialLogin') as HTMLButtonElement;

        this.initializeEventListeners();
        this.checkSavedCredentials();
    }

    private initializeEventListeners(): void {
        this.form.addEventListener('submit', this.handleSubmit.bind(this));
        this.socialLoginButton.addEventListener('click', this.handleSocialLogin.bind(this));
    }

    private checkSavedCredentials(): void {
        const savedEmail = localStorage.getItem('savedEmail');
        if (savedEmail) {
            this.emailInput.value = savedEmail;
            this.rememberMeCheckbox.checked = true;
        }
    }

    private async handleSubmit(event: Event): Promise<void> {
        event.preventDefault();

        if (!this.form.checkValidity()) {
            event.stopPropagation();
            this.form.classList.add('was-validated');
            return;
        }

        const loginData: LoginRequest = {
            email: this.emailInput.value,
            password: this.passwordInput.value,
            rememberMe: this.rememberMeCheckbox.checked
        };

        try {
            const response = await this.submitLogin(loginData);
            if (response.success) {
                this.handleLoginSuccess(response);
            } else {
                this.handleLoginError(response.message);
            }
        } catch (error) {
            console.error('로그인 중 오류가 발생했습니다:', error);
            this.handleLoginError('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
    }

    private async submitLogin(data: LoginRequest): Promise<LoginResponse> {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || '로그인에 실패했습니다.');
        }

        return response.json();
    }

    private handleLoginSuccess(response: LoginResponse): void {
        if (response.token) {
            // JWT 토큰 저장
            localStorage.setItem('token', response.token);
            
            // 자동 로그인 설정
            if (this.rememberMeCheckbox.checked) {
                localStorage.setItem('savedEmail', this.emailInput.value);
            } else {
                localStorage.removeItem('savedEmail');
            }

            // 사용자 정보 저장
            if (response.user) {
                localStorage.setItem('user', JSON.stringify(response.user));
            }

            // 메인 페이지로 이동
            window.location.href = '/';
        }
    }

    private handleLoginError(message: string): void {
        alert(message);
    }

    private handleSocialLogin(): void {
        // Google OAuth 로그인 구현
        window.location.href = '/oauth2/authorization/google';
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new LoginForm();
}); 