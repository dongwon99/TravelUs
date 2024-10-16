import React, { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router";
import { accountApi } from "../../../../api/account";
import { userApi } from "../../../../api/user";
import { AccountInfoNew, MeetingAccountInfo } from "../../../../types/account";
import { IoIosArrowBack } from "react-icons/io";
import Loading from "../../../../components/loading/Loading";

interface TransferConfirmProps {
  // Define the props for the component here
}

const FillConfirm: React.FC<TransferConfirmProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { groupId } = useParams();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [userName, setUserName] = useState<string>("");
  const { generalAccountNo, meetingAccountNo, transferAmount } = location.state as {
    generalAccountNo: string;
    meetingAccountNo: string;
    transferAmount: string;
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 사용자 이름을 위한 API 호출
        const userResponse = await userApi.fetchUser();
        setUserName(userResponse.data.name);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
    fetchSpecificMeetingAccount();
  }, []);

  if (!meeting && !userName) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div>
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meeting/${meeting?.groupId}/fill/setmoney`);
            }}
            className="text-2xl"
          />
        </div>
        <div className="mb-16 flex flex-col items-center">
          <p className="text-2xl font-bold">
            {meeting?.groupName}
            <span className="font-normal"> 에</span>
          </p>
          <p className="text-2xl font-bold">{formatCurrency(parseInt(transferAmount))}원</p>
          <p className="text-2xl">채울까요?</p>
        </div>
        <div className="flex flex-col space-y-6">
          <div className="flex flex-col space-y-2">
            <div className="flex justify-between">
              <p className="text-zinc-500">출금 계좌</p>
              <p>{generalAccountNo}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">입금 계좌</p>
              <p>{meetingAccountNo}</p>
            </div>
          </div>
          <div className="flex flex-col space-y-3">
            <button
              onClick={() => {
                navigate(`/meeting/${meeting?.groupId}/fill/password`, {
                  state: { meetingAccountNo, transferAmount, generalAccountNo },
                });
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide text-white bg-[#1429A0]">
              채우기
            </button>
            <button
              onClick={() => {
                navigate(`/meeting/${groupId}/fill/setmoney`);
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FillConfirm;
