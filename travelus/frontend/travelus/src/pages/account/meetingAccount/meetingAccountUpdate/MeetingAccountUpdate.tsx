import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import MeetingNameInputMui from "../../../../components/meetingAccount/MeetingNameInputMui";
import MeetingTypeInputMui from "../../../../components/meetingAccount/MeetingTypeInputMui";
import { useNavigate, useParams } from "react-router";
import { useDispatch } from "react-redux";
import { accountApi } from "../../../../api/account";
import { MeetingAccountInfo, MeetingAccountDetailInfo } from "../../../../types/account";
import Loading from "../../../../components/loading/Loading";

const MeetingAccountUpdate = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [menuStep, setMenuStep] = useState(1); // 1 : 모임명, 2 : 모임종류
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);

  const [name, setName] = useState("");
  const [type, setType] = useState("");

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);
        setName(meetingData.groupName);
        setType(meetingData.icon);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, [id]);

  const handleUpdate = () => {
    // 모임 수정 API 호출
    const updateMeetingAccount = async () => {
      if (!meeting) {
        return;
      }

      try {
        const data = {
          groupAccountNo: meeting?.groupAccountNo,
          travelStartDate: "2024-10-10",
          travelEndDate: "2024-10-12",
          groupName: name,
          icon: type,
        };

        const response = await accountApi.fetchUpdateMeetingAccount(Number(id), data);

        if (response.status === 200) {
          navigate(`/meetingaccount/management/${id}`);
        }
      } catch (error) {
        console.error("모임 수정 에러", error);
      }
    };

    updateMeetingAccount();
  };

  const handleNextStep = () => {
    setMenuStep(menuStep + 1);
  };

  if (!meeting) {
    return <Loading />;
  }

  return (
    <div className="h-full p-6 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack onClick={() => navigate(`/meetingaccount/management/${id}`)} className="text-2xl" />
        </div>

        <div className="text-2xl font-bold">아이콘 · 모임통장명</div>

        <div className="flex flex-col space-y-10">
          <MeetingTypeInputMui meetingType={type} setMeetingType={setType} />
          <MeetingNameInputMui meetingName={name} setMeetingName={setName} onInputComplete={handleNextStep} />
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0] ${
          name === "" || type === "" ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
        }`}
        onClick={() => handleUpdate()}
        disabled={name === "" || type === ""}>
        변경
      </button>
    </div>
  );
};

export default MeetingAccountUpdate;
