import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { accountApi } from "../../../api/account";
import { MeetingAccountInfo } from "../../../types/account";

const CompletedOfCreateCard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const { groupId } = useParams();

  useEffect(() => {
    // meeting 정보 가져오는 API 호출
    const fetchMeeting = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          setMeeting(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchMeeting();
  }, [groupId]);

  const handleNext = () => {
    navigate("/");
  };

  return (
    <>
      {meeting && (
        <div className="h-full p-5 pb-8">
          <div className="h-full flex flex-col justify-between">
            <div className="h-full mt-32 flex flex-col items-center space-y-5">
              <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

              <div className="text-[1.4rem] flex flex-col justify-center items-center">
                <p className="">{meeting?.groupName}의</p>
                <div className="flex">
                  <p className="text-[#1429A0] font-semibold">모임 카드</p>
                  <p>가</p>
                </div>
                <p className="text-center">개설되었어요</p>
              </div>
            </div>

            <div className="flex flex-col space-y-3">
              <button
                onClick={() => {
                  handleNext();
                }}
                className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]">
                확인
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default CompletedOfCreateCard;
