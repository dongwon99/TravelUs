import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { userApi } from "../../../api/user";
import { accountApi } from "../../../api/account";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { MeetingAccountInfo } from "../../../types/account";
import Loading from "../../../components/loading/Loading";

const CompletedOfDeleteTravelBox = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const { accountNo } = useParams();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);

  useEffect(() => {
    // 해당 모임 조회 API 호출
    const fetchSpecificMeetingAccount = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          setMeeting(response.data);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchSpecificMeetingAccount();
  }, [groupId]);

  const handleNext = () => {
    navigate(`/meetingaccount/${groupId}`);
  };

  if (!meeting) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl flex flex-col justify-center">
            <div className="flex flex-col items-center">
              <p>{meeting.groupName}</p>
              <p className="text-[#1429A0] font-semibold">
                외화저금통
                <span className="text-black font-normal">이</span>
              </p>
            </div>

            <p className="text-center">해지되었어요</p>
          </div>
        </div>

        <div className="flex flex-col space-y-3">
          <button
            onClick={() => {
              handleNext();
            }}
            className={`w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]`}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default CompletedOfDeleteTravelBox;
