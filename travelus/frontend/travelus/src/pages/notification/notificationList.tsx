import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { BiTransferAlt } from "react-icons/bi";
import { RiHandCoinFill } from "react-icons/ri";
import { IoPerson } from "react-icons/io5";
import { BsFillPeopleFill } from "react-icons/bs";
import { RiExchangeDollarLine } from "react-icons/ri";
import { notificationApi } from "../../api/notification";
import { NotificationListInfo } from "../../types/notification";
import Loading from "../../components/loading/Loading";
import { tr } from "date-fns/locale";

interface NotificationListProps {
  // Define props here
}

const NotificationList: React.FC<NotificationListProps> = (props) => {
  const navigate = useNavigate();
  const [notificationList, setNotificationList] = useState<NotificationListInfo[] | null>(null);

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
          console.log("알림 리스트", sortedNotifications);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchNotificaitonList();
  }, []);

  // 날짜를 "10월 1일" 형식으로 변환하는 함수
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat("ko-KR", {
      month: "long",
      day: "numeric",
    }).format(date);
  };

  // notificationType에 따른 아이콘을 반환하는 함수
  const renderIcon = (type: string) => {
    switch (type) {
      case "S":
        return <RiHandCoinFill className="text-xl text-[#f3ba4f]" />;
      case "E":
        return <RiExchangeDollarLine className="text-2xl text-[#27995a]" />;
      case "GT":
        return <BsFillPeopleFill className="text-xl text-[#699BF7]" />;
      default:
        return <IoPerson className="text-xl text-gray-400" />; // 기본 아이콘
    }
  };

  const navigateToDetail = async (
    type: string,
    accountNo: string,
    notificationId: number,
    groupId?: number,
    currencyCode?: string
  ) => {
    try {
      const response = await notificationApi.deleteSpecificNotification(notificationId);
    } catch (error) {
      console.error("알림 삭제 실패", error);
    }

    switch (type) {
      case "S":
        navigate(`/settlement/expenditure/list/NOT_COMPLETED`);
        break;
      case "E":
        navigate(`/travelbox/transaction/${accountNo}/notification?groupId=${groupId}&currencyCode=${currencyCode}`);
        break;
      case "GT":
        navigate(`/meetingtransaction/${accountNo}/notification`);
        break;
      default:
        navigate(`/transaction/${accountNo}`);
        break;
    }
  };

  if (!notificationList) {
    return <Loading />;
  }

  return (
    <div className="min-h-screen p-6 pb-8">
      <div className="flex flex-col space-y-10">
        <IoIosArrowBack
          onClick={() => {
            navigate("/");
          }}
          className="text-2xl"
        />

        <div className="w-full space-y-9">
          <p className="text-3xl font-bold">알림</p>
          {notificationList.length > 0 ? (
            notificationList.map((notification) => (
              <div
                onClick={() =>
                  notification.notificationType === "E"
                    ? navigateToDetail(
                        notification.notificationType,
                        notification.accountNo,
                        notification.notificationId,
                        notification.groupId,
                        notification.currencyCode
                      )
                    : navigateToDetail(
                        notification.notificationType,
                        notification.accountNo,
                        notification.notificationId
                      )
                }
                key={notification.notificationId} // Fragment 제거하고 key 유지
                className="space-y-1">
                <div className="flex items-start space-x-2">
                  {renderIcon(notification.notificationType)}
                  <div className="w-full flex flex-col space-y-[0.4rem]">
                    <div className="flex justify-between items-center">
                      <p className="text-sm text-zinc-500">{notification.title}</p>
                      <p className="text-sm text-zinc-400">{formatDate(notification.createdAt)}</p>
                    </div>
                    <p className=" text-gray-600">{notification.message}</p>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="pt-64 text-lg text-zinc-700 flex justify-center items-center">
              <p>아직 받은 알림이 없어요</p>
            </div>
          )}

          {notificationList.length > 0 && (
            <div className="pb-16 pt-3 grid grid-cols-[20%_60%_20%] items-center">
              <div className="w-full h-[0.1rem] bg-zinc-200"></div>
              <p className="w-full text-center text-sm text-zinc-400">7일전 알림까지 확인할 수 있어요</p>
              <div className="w-full h-[0.1rem] bg-zinc-200"></div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default NotificationList;
