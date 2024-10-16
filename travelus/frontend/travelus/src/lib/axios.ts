import axios from "axios"
import { isTokenExpired, parseJwt } from "../utils/jwtUtil";

// Axios 요청
const api = axios.create({
  // baseURL: 'https://soltravel.shop/api/v1',
  baseURL: 'https://j11d209.p.ssafy.io/api/v2',
  // baseURL: 'http://localhost:8082/api/v2',
});

// 요청 인터셉터
api.interceptors.request.use(
  config => {
    // // accessToken을 sessionStorage에서 가져오고
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      if (isTokenExpired(accessToken)) {
        // 토큰이 만료된 경우 처리
        console.error("토큰이 만료되었습니다. 다시 로그인해주세요.");
        localStorage.removeItem("accessToken");
        window.location.href = "/login"; // 로그인 페이지로 리다이렉트
        return Promise.reject(new Error("토큰이 만료되었습니다."));
      }

      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config
  },
  error => Promise.reject(error),
);

// 응답 인터셉터

// 응답 인터셉터
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      // FCM 토큰 삭제 API 호출
      try {
        const userId = localStorage.getItem("userId");
        if (userId) {
          await api.delete(`/fcmToken/${userId}`);
          console.log(`FCM token for user ${userId} has been deleted.`);
        }
      } catch (deleteError) {
        console.error("FCM 토큰 삭제 실패:", deleteError);
      }

      // 토큰이 만료되었거나 유효하지 않은 경우 처리
      localStorage.removeItem("accessToken");
      localStorage.removeItem("userId");
      localStorage.removeItem("userName");
      window.location.href = "/login"; // 로그인 페이지로 리다이렉트
    }
    return Promise.reject(error);
  }
);

export default api