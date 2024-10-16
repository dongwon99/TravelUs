import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { setMeetingAccountInfo, setTravelboxInfo } from "../../../redux/meetingAccountSlice";
import { groupApi } from "../../../api/group";
import { useLocation, useNavigate, useParams } from "react-router";

const CreateRequestOfCreateMeetingAccount = () => {
  const params = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();

  const meetingName = useSelector((state: RootState) => state.meetingAccount.meetingName);
  const meetingType = useSelector((state: RootState) => state.meetingAccount.meetingType);
  const individualAccountNo = useSelector((state: RootState) => state.meetingAccount.individualAccountNo);

  const handleCreate = async () => {
    const data = {
      groupAccountPassword: location.state.password,
      groupName: meetingName,
      icon: meetingType,
      travelStartDate: "2024-01-01",
      travelEndDate: "2024-01-07",
      personalAccountNo: individualAccountNo,
    };

    try {
      const response = await groupApi.createMeetingAccount(data);
      if (response.status === 201) {
        dispatch(
          setMeetingAccountInfo({
            groupAccountPassword: "",
            groupName: response.data.groupName,
            icon: response.data.icon,
            groupId: response.data.groupId,
          })
        );
        dispatch(
          setTravelboxInfo({
            accountPassword: "",
            accountNo: response.data.groupAccountNo,
            currencyCode: "",
          })
        );
        navigate(`/meeting/create/completed/meeting`);
      }
    } catch (error) {
      alert("모임통장 개설에 실패했어요");
      navigate("/");
      console.log(error);
    }
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="mt-28 flex flex-col items-center space-y-3">
          <img className="w-2/3" src="/assets/IDSuccess.png" alt="" />

          <div className="text-center grid gap-3">
            <p className="text-2xl font-semibold">본인인증을 완료했어요</p>
            <p className="text-sm text-[#6F7581]">마지막으로 모임통장 개설을 완료해주세요</p>
          </div>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleCreate()}>
        모임통장 개설하기
      </button>
    </div>
  );
};

export default CreateRequestOfCreateMeetingAccount;
