import { getToken, onMessage, getMessaging } from "firebase/messaging";
import { messaging } from "../firebase";
import { notificationApi } from "../api/notification";

// FCM 서비스 워커 등록
export const registerServiceWorker = async () => {
  try {
    const registration = await navigator.serviceWorker.register("firebase-messaging-sw.js");
    console.log("Service Worker 등록 성공:", registration);

    // initializeFcmAndRegisterToken();

  } catch (error) {
    console.error("Service Worker 등록 실패:", error);
  }
};

// 알림 권한 요청
export const requestPermission = async () => {
  console.log("권한 요청 중...");
  const permission = await Notification.requestPermission();
  const granted = permission === "granted";

  return granted;
};

// 토큰 발급 및 저장
export const fetchAndStoreFcmToken = async () => {
  try {
    const currentToken = await getToken(messaging, {
      vapidKey: process.env.REACT_APP_VAPID_KEY,
    });

    if (!currentToken) {
      console.warn("토큰을 생성할 수 없습니다. 권한을 요청해보세요.");
      return null;
    }

    // FCM 토큰을 서버에 저장
    await notificationApi.fetchFcmToken(currentToken);

    return currentToken;
  } catch (err) {
    console.error("FCM 토큰 발급 및 저장 중 오류 발생: ", err);
    throw err;
  }
};

// FCM 초기 설정
export const initializeFcmAndRegisterToken = async () => {
  try {
    const permissionGranted = await requestPermission();
    if (permissionGranted) {
      const token = await fetchAndStoreFcmToken();

      console.log("토큰 저장 성공 : ");


    } else {
      console.warn("알림 권한이 허용되지 않았습니다.");
    }
  } catch (error) {
    console.error("FCM 설정 중 오류 발생:", error);
  }
};

// 포그라운드 수신 함수
export const setupFirebaseMessaging = async () => {
  const messaging = getMessaging();

  // 브라우저가 열려 있을 때 푸시 메시지 수신
  onMessage(messaging, (payload) => {
    console.log('푸시 메시지 수신:', payload);

    if (Notification.permission === "granted") {
      const title = payload.notification?.title || '기본 제목';
      const body = payload.notification?.body || '기본 메시지 내용';

      // 알림을 표시할 수 있는 조건을 확인한 후 알림 생성
      if (title && body) {
        new Notification(title, {
          body: body,
          icon: '/icons/icon-192x192.png',
          badge: '/icons/badge-72x72.png',
        });
      } else {
        console.warn('푸시 메시지에 알림 정보가 없습니다.');
      }
    } else {
      console.warn("Notification 권한이 없습니다.");
    }
  });
};