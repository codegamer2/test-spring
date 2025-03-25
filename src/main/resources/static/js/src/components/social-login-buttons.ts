import { SocialLoginHandler } from '../auth/social-login';

export function createSocialLoginButtons(): string {
    const handler = SocialLoginHandler.getInstance();
    
    return `
        <div class="social-login-buttons">
            <button type="button" class="btn btn-social btn-google" onclick="handleSocialLogin('google')">
                <img src="/images/google.svg" alt="Google" width="20" height="20" class="me-2">
                Google로 계속하기
            </button>
            <button type="button" class="btn btn-social btn-naver" onclick="handleSocialLogin('naver')">
                <img src="/images/naver.svg" alt="Naver" width="20" height="20" class="me-2">
                네이버로 계속하기
            </button>
            <button type="button" class="btn btn-social btn-kakao" onclick="handleSocialLogin('kakao')">
                <img src="/images/kakao.svg" alt="Kakao" width="20" height="20" class="me-2">
                카카오로 계속하기
            </button>
        </div>
        <script>
            async function handleSocialLogin(provider) {
                const handler = SocialLoginHandler.getInstance();
                await handler.handleSocialLogin(provider);
            }
        </script>
    `;
} 