import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { IoNotificationsOutline } from "react-icons/io5";
import { IoIosArrowForward } from "react-icons/io";
import { userApi } from "../../api/user";
import { notificationApi } from "../../api/notification";
import { NotificationListInfo } from "../../types/notification";

const Header = () => {
  const navigate = useNavigate();
  const [userName, setUserName] = useState("");
  const [notificationList, setNotificationList] = useState<NotificationListInfo[] | null>(null);

  useEffect(() => {
    // 유정 정보 가져오는 API 호출
    const fetchUser = async () => {
      try {
        const response = await userApi.fetchUser();
        if (response.status === 200) {
          setUserName(response.data.name);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchUser();
  }, []);

  useEffect(() => {
    // 알림 리스트를 가져오는 API 호출
    const fetchNotificaitonList = async () => {
      try {
        const response = await notificationApi.fetchNotificationList();
        if (response.status === 200) {
          const sortedNotifications = response.data.sort(
            (a: NotificationListInfo, b: NotificationListInfo) =>
              new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
          setNotificationList(sortedNotifications);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchNotificaitonList();
  }, []);

  return (
    <div className="h-16 p-5 bg-[#F3F4F6] sticky top-0 flex justify-between items-center z-50">
      <button
        onClick={() => {
          navigate("/mypage");
        }}
        className="text-xl flex items-center space-x-2">
        <p className="text-[#565656] font-semibold">{userName} 님</p>
        <IoIosArrowForward className="text-[#565656]" />
      </button>
      <button
        onClick={() => {
          navigate("/notification");
        }}
        className="relative">
        <IoNotificationsOutline className="text-2xl" />
        {notificationList && notificationList.length > 0 && (
          <span className="absolute top-[0.1rem] -right-[0.15rem] w-[0.3rem] h-[0.3rem] bg-[#f35d5d] rounded-full"></span>
        )}
      </button>
    </div>
  );
};

export default Header;
