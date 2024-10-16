import api from "../lib/axios";
import { NotificationData } from "../types/notification";

export const notificationApi = {
  // fcm 토큰 저장
  fetchFcmToken: (fcmToken: string) => {
    return api.post(`/notification/fcmToken`, { fcmToken });
  },

  // fcm 토큰 저장
  deleteFcmToken: (userId: Number) => {
    return api.delete(`/notification/fcmToken/${userId}`);
  },

  // 푸시 알림 보내기 (포그라운드 메시지 수신 테스트용)
  pushNotification: (notificationData: NotificationData) => {
    return api.post(`/notification/push`, notificationData);
  },

  // 알림 리스트 조회
  fetchNotificationList: () => {
    return api.get(`/notification/all`);
  },

  // 특정 알림 삭제
  deleteSpecificNotification: (notificationId: number) => {
    return api.delete(`/notification/${notificationId}`);
  },
};
