import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import MeetingNameInputMui from "../../../components/meetingAccount/MeetingNameInputMui";
import MeetingTypeInputMui from "../../../components/meetingAccount/MeetingTypeInputMui";
import { useNavigate } from "react-router";
import { useDispatch } from "react-redux";
import { setMeetingName, setMeetingType } from "../../../redux/meetingAccountSlice";

const MeetingInfoOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [menuStep, setMenuStep] = useState(1); // 1 : 모임명, 2 : 모임종류
  const guideText = [
    ["모임명을", "입력해주세요"],
    ["모임종류를", "선택해주세요"],
  ];

  const [name, setName] = useState("");
  const [type, setType] = useState("");

  const handleNext = () => {
    dispatch(setMeetingName(name));
    dispatch(setMeetingType(type));

    navigate("/meeting/create/select/account");
  };

  const handleNextStep = () => {
    if (menuStep < guideText.length) {
      setMenuStep(menuStep + 1);
    }
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={()=>{navigate("/meeting/create/userinfo")}}
            className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#1429A0] font-semibold">01</p>
              <p className="font-medium">모임통장 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>{guideText[menuStep - 1][0]}</p>
              <p>{guideText[menuStep - 1][1]}</p>
            </div>
          </div>

          {menuStep >= 2 ? <MeetingTypeInputMui meetingType={type} setMeetingType={setType} /> : <></>}
          {menuStep >= 1 ? (
            <MeetingNameInputMui meetingName={name} setMeetingName={setName} onInputComplete={handleNextStep} />
          ) : (
            <></>
          )}
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0] ${
          name === "" || type === "" ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
        }`}
        onClick={() => handleNext()}
        disabled={name === "" || type === ""}>
        다음
      </button>
    </div>
  );
};

export default MeetingInfoOfCreateMeetingAccount;
