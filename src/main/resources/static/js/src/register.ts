import { RegisterRequest, RegisterResponse, ValidationError } from './types/user';

class RegisterForm {
    private form: HTMLFormElement;
    private emailInput: HTMLInputElement;
    private passwordInput: HTMLInputElement;
    private confirmPasswordInput: HTMLInputElement;
    private nicknameInput: HTMLInputElement;
    private genreCheckboxes: NodeListOf<HTMLInputElement>;
    private socialRegisterButton: HTMLButtonElement;

    constructor() {
        this.form = document.getElementById('registerForm') as HTMLFormElement;
        this.emailInput = document.getElementById('email') as HTMLInputElement;
        this.passwordInput = document.getElementById('password') as HTMLInputElement;
        this.confirmPasswordInput = document.getElementById('confirmPassword') as HTMLInputElement;
        this.nicknameInput = document.getElementById('nickname') as HTMLInputElement;
        this.genreCheckboxes = document.querySelectorAll('input[type="checkbox"]');
        this.socialRegisterButton = document.getElementById('socialRegister') as HTMLButtonElement;

        this.initializeEventListeners();
    }

    private initializeEventListeners(): void {
        this.form.addEventListener('submit', this.handleSubmit.bind(this));
        this.socialRegisterButton.addEventListener('click', this.handleSocialRegister.bind(this));
        this.passwordInput.addEventListener('input', this.validatePasswordMatch.bind(this));
        this.confirmPasswordInput.addEventListener('input', this.validatePasswordMatch.bind(this));
    }

    private validatePasswordMatch(): void {
        const password = this.passwordInput.value;
        const confirmPassword = this.confirmPasswordInput.value;

        if (password !== confirmPassword) {
            this.confirmPasswordInput.setCustomValidity('비밀번호가 일치하지 않습니다.');
        } else {
            this.confirmPasswordInput.setCustomValidity('');
        }
    }

    private getSelectedGenres(): string[] {
        return Array.from(this.genreCheckboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);
    }

    private async handleSubmit(event: Event): Promise<void> {
        event.preventDefault();

        if (!this.form.checkValidity()) {
            event.stopPropagation();
            this.form.classList.add('was-validated');
            return;
        }

        const selectedGenres = this.getSelectedGenres();
        if (selectedGenres.length === 0) {
            alert('최소 하나의 관심 장르를 선택해주세요.');
            return;
        }

        const registerData: RegisterRequest = {
            email: this.emailInput.value,
            password: this.passwordInput.value,
            nickname: this.nicknameInput.value,
            genres: selectedGenres
        };

        try {
            const response = await this.submitRegistration(registerData);
            if (response.success) {
                alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/login';
            } else {
                alert(response.message);
            }
        } catch (error) {
            console.error('회원가입 중 오류가 발생했습니다:', error);
            alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
    }

    private async submitRegistration(data: RegisterRequest): Promise<RegisterResponse> {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || '회원가입에 실패했습니다.');
        }

        return response.json();
    }

    private handleSocialRegister(): void {
        // Google OAuth 로그인 구현
        window.location.href = '/oauth2/authorization/google';
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new RegisterForm();
}); 