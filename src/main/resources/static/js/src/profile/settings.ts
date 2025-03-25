import { ProfileUpdateRequest, ProfileUpdateResponse, ProfileError, ProfileImageUploadResponse } from '../types/profile';
import { SocialLoginHandler } from '../auth/social-login';

export class ProfileSettings {
    private form!: HTMLFormElement;
    private profileImage!: HTMLImageElement;
    private profileImageInput!: HTMLInputElement;
    private nicknameInput!: HTMLInputElement;
    private bioInput!: HTMLTextAreaElement;
    private genreCheckboxes!: NodeListOf<HTMLInputElement>;
    private passwordInput!: HTMLInputElement;
    private confirmPasswordInput!: HTMLInputElement;
    private submitButton!: HTMLButtonElement;
    private logoutButton!: HTMLAnchorElement;
    private socialLoginHandler: SocialLoginHandler;

    constructor() {
        this.initializeElements();
        this.initializeEventListeners();
        this.socialLoginHandler = SocialLoginHandler.getInstance();
    }

    private initializeElements(): void {
        this.form = document.getElementById('profileForm') as HTMLFormElement;
        this.profileImage = document.getElementById('profileImage') as HTMLImageElement;
        this.profileImageInput = document.getElementById('profileImageInput') as HTMLInputElement;
        this.nicknameInput = document.getElementById('nickname') as HTMLInputElement;
        this.bioInput = document.getElementById('bio') as HTMLTextAreaElement;
        this.genreCheckboxes = document.querySelectorAll('.genre-checkboxes input[type="checkbox"]');
        this.passwordInput = document.getElementById('password') as HTMLInputElement;
        this.confirmPasswordInput = document.getElementById('confirmPassword') as HTMLInputElement;
        this.submitButton = this.form.querySelector('button[type="submit"]') as HTMLButtonElement;
        this.logoutButton = document.getElementById('logoutBtn') as HTMLAnchorElement;
    }

    private initializeEventListeners(): void {
        this.form.addEventListener('submit', this.handleSubmit.bind(this));
        this.profileImageInput.addEventListener('change', this.handleProfileImageChange.bind(this));
        this.logoutButton.addEventListener('click', this.handleLogout.bind(this));
    }

    private async handleProfileImageChange(event: Event): Promise<void> {
        const file = (event.target as HTMLInputElement).files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('image', file);

        try {
            this.submitButton.disabled = true;
            const response = await fetch('/api/profile/image', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                throw new Error('프로필 이미지 업로드에 실패했습니다.');
            }

            const data: ProfileImageUploadResponse = await response.json();
            this.profileImage.src = data.imageUrl;
            this.showSuccessMessage('프로필 이미지가 업데이트되었습니다.');
        } catch (error) {
            console.error('프로필 이미지 업로드 중 오류 발생:', error);
            this.showErrorMessage('프로필 이미지 업로드에 실패했습니다.');
        } finally {
            this.submitButton.disabled = false;
        }
    }

    private async handleSubmit(event: Event): Promise<void> {
        event.preventDefault();

        if (!this.form.checkValidity()) {
            event.stopPropagation();
            this.form.classList.add('was-validated');
            return;
        }

        if (this.passwordInput.value && this.passwordInput.value !== this.confirmPasswordInput.value) {
            this.showErrorMessage('비밀번호가 일치하지 않습니다.');
            return;
        }

        const selectedGenres = Array.from(this.genreCheckboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);

        const request: ProfileUpdateRequest = {
            nickname: this.nicknameInput.value,
            bio: this.bioInput.value,
            genres: selectedGenres,
            password: this.passwordInput.value || undefined,
        };

        try {
            this.submitButton.disabled = true;
            const response = await fetch('/api/profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            });

            if (!response.ok) {
                const error: ProfileError = await response.json();
                throw new Error(error.message);
            }

            const data: ProfileUpdateResponse = await response.json();
            this.updateProfileDisplay(data.user);
            this.showSuccessMessage('프로필이 업데이트되었습니다.');
            this.passwordInput.value = '';
            this.confirmPasswordInput.value = '';
        } catch (error) {
            console.error('프로필 업데이트 중 오류 발생:', error);
            this.showErrorMessage(error instanceof Error ? error.message : '프로필 업데이트에 실패했습니다.');
        } finally {
            this.submitButton.disabled = false;
        }
    }

    private updateProfileDisplay(user: ProfileUpdateResponse['user']): void {
        document.getElementById('nicknameDisplay')!.textContent = user.nickname;
        if (user.profileImage) {
            this.profileImage.src = user.profileImage;
        }
    }

    private handleLogout(event: Event): void {
        event.preventDefault();
        this.socialLoginHandler.logout();
    }

    private showSuccessMessage(message: string): void {
        // TODO: 토스트 메시지 구현
        alert(message);
    }

    private showErrorMessage(message: string): void {
        // TODO: 토스트 메시지 구현
        alert(message);
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new ProfileSettings();
}); 