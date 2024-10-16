import { UserInfo } from "../types/userInformation";
import api from "../lib/axios";

export const userApi = {
  // 회원가입
  fetchSignUp: (formData: FormData) => {
    return api.post(`/user/join`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 아이디 중복 조회
  fetchValidateId: (id: string) => {
    return api.post(`/user/dup-check`, { id });
  },
    
  // SMS 인증 전송
  fetchSendSmsValidation: (phone: string) => {
    return api.post(`/auth/verify/phone/send`, { phone });
  },

  // 인증 검사
  fetchVerifySmsCode: (phone: string, authCode: string) => {

    return api.post(`/auth/verify/phone/code`, { phone, authCode });
  },

  // 로그인
  fetchLogin: (id: string, password: string) => {
    return api.post(`/auth/login`, { id, password });
  },

  // 모임원 초대 시 이메일 유효성 검사
  fetchEmailValidation: (email: string) => {
    return api.get(`/user/validate-email/${email}`);
  },

  // 유저 조회
  fetchUser: () => {
    return api.get(`/user/search`);
  },

  // 유저 프로필 이미지 수정
  uploadProfileImage: (formData: FormData) => {
    return api.post(`/user`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 유저 정보 수정
  editUserInformation: (formData: FormData) => {
    return api.post(`/user/update`, formData);
  },

  // 신분증 인식
  fetchIdcard: (formData: FormData) => {
    return api.post(`/auth/id-card`, formData);
  },
};