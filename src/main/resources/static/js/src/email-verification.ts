import { EmailVerificationRequest, EmailVerificationResponse, EmailVerificationConfirmRequest, EmailVerificationConfirmResponse } from './types/email';

class EmailVerification {
    private form: HTMLFormElement;
    private emailInput: HTMLInputElement;
    private verificationCodeInput: HTMLInputElement;
    private sendVerificationBtn: HTMLButtonElement;
    private verifyCodeBtn: HTMLButtonElement;
    private emailInputSection: HTMLElement;
    private verificationCodeSection: HTMLElement;
    private verificationSuccessSection: HTMLElement;
    private timerElement: HTMLElement;
    private timer: number | null = null;
    private timeLeft: number = 180; // 3분

    constructor() {
        this.form = document.getElementById('emailVerificationForm') as HTMLFormElement;
        this.emailInput = document.getElementById('email') as HTMLInputElement;
        this.verificationCodeInput = document.getElementById('verificationCode') as HTMLInputElement;
        this.sendVerificationBtn = document.getElementById('sendVerificationBtn') as HTMLButtonElement;
        this.verifyCodeBtn = document.getElementById('verifyCodeBtn') as HTMLButtonElement;
        this.emailInputSection = document.getElementById('emailInputSection') as HTMLElement;
        this.verificationCodeSection = document.getElementById('verificationCodeSection') as HTMLElement;
        this.verificationSuccessSection = document.getElementById('verificationSuccessSection') as HTMLElement;
        this.timerElement = document.getElementById('timer') as HTMLElement;

        this.initializeEventListeners();
    }

    private initializeEventListeners(): void {
        this.sendVerificationBtn.addEventListener('click', this.handleSendVerification.bind(this));
        this.verifyCodeBtn.addEventListener('click', this.handleVerifyCode.bind(this));
        this.emailInput.addEventListener('input', this.validateEmail.bind(this));
        this.verificationCodeInput.addEventListener('input', this.validateVerificationCode.bind(this));
    }

    private validateEmail(): boolean {
        const email = this.emailInput.value;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const isValid = emailRegex.test(email);

        this.emailInput.classList.toggle('is-invalid', !isValid);
        this.sendVerificationBtn.disabled = !isValid;

        return isValid;
    }

    private validateVerificationCode(): boolean {
        const code = this.verificationCodeInput.value;
        const isValid = code.length === 6;

        this.verificationCodeInput.classList.toggle('is-invalid', !isValid);
        this.verifyCodeBtn.disabled = !isValid;

        return isValid;
    }

    private async handleSendVerification(): Promise<void> {
        if (!this.validateEmail()) {
            return;
        }

        const request: EmailVerificationRequest = {
            email: this.emailInput.value
        };

        try {
            this.sendVerificationBtn.disabled = true;
            const response = await this.sendVerificationEmail(request);
            
            if (response.success) {
                this.showVerificationCodeSection();
                this.startTimer();
            } else {
                alert(response.message);
            }
        } catch (error) {
            console.error('인증번호 전송 중 오류가 발생했습니다:', error);
            alert('인증번호 전송 중 오류가 발생했습니다. 다시 시도해주세요.');
        } finally {
            this.sendVerificationBtn.disabled = false;
        }
    }

    private async handleVerifyCode(): Promise<void> {
        if (!this.validateVerificationCode()) {
            return;
        }

        const request: EmailVerificationConfirmRequest = {
            email: this.emailInput.value,
            token: this.verificationCodeInput.value
        };

        try {
            this.verifyCodeBtn.disabled = true;
            const response = await this.verifyCode(request);
            
            if (response.success) {
                this.showVerificationSuccess();
            } else {
                alert(response.message);
            }
        } catch (error) {
            console.error('인증번호 확인 중 오류가 발생했습니다:', error);
            alert('인증번호 확인 중 오류가 발생했습니다. 다시 시도해주세요.');
        } finally {
            this.verifyCodeBtn.disabled = false;
        }
    }

    private async sendVerificationEmail(data: EmailVerificationRequest): Promise<EmailVerificationResponse> {
        const response = await fetch('/api/auth/email/verification', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || '인증번호 전송에 실패했습니다.');
        }

        return response.json();
    }

    private async verifyCode(data: EmailVerificationConfirmRequest): Promise<EmailVerificationConfirmResponse> {
        const response = await fetch('/api/auth/email/verification/confirm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || '인증번호 확인에 실패했습니다.');
        }

        return response.json();
    }

    private showVerificationCodeSection(): void {
        this.emailInputSection.classList.add('d-none');
        this.verificationCodeSection.classList.remove('d-none');
    }

    private showVerificationSuccess(): void {
        this.verificationCodeSection.classList.add('d-none');
        this.verificationSuccessSection.classList.remove('d-none');
    }

    private startTimer(): void {
        this.timeLeft = 180;
        this.updateTimerDisplay();

        this.timer = window.setInterval(() => {
            this.timeLeft--;
            this.updateTimerDisplay();

            if (this.timeLeft <= 0) {
                this.stopTimer();
                alert('인증번호가 만료되었습니다. 다시 시도해주세요.');
            }
        }, 1000);
    }

    private stopTimer(): void {
        if (this.timer) {
            clearInterval(this.timer);
            this.timer = null;
        }
    }

    private updateTimerDisplay(): void {
        const minutes = Math.floor(this.timeLeft / 60);
        const seconds = this.timeLeft % 60;
        this.timerElement.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new EmailVerification();
}); 