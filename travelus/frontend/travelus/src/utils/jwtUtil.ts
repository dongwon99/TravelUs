
// 토큰 파서
export const parseJwt = (token: String) => {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );

        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Invalid token', error);
        return null;
    }
};

// 토큰 파싱 후 만료 여부 체크
export const isTokenExpired = (token: String) => {
    const payload = parseJwt(token);
    if (payload && payload.exp) {
        const currentTime = Math.floor(new Date().getTime() / 1000); // 현재 시간 (초 단위)
        return payload.exp < currentTime; // 만료 시간이 현재 시간보다 작으면 만료됨
    }
    return true; // 만료 정보가 없으면 만료된 것으로 간주
};