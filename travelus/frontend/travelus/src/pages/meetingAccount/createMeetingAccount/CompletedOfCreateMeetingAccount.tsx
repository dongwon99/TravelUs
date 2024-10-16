import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { userApi } from "../../../api/user";
import { accountApi } from "../../../api/account";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store";

const CompletedOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const params = useParams();

  const [userName, setUserName] = useState("");
  const [groupCode, setGroupCode] = useState("");
  const meetingAccountInfo = useSelector((state: RootState) => state.meetingAccount.meetingAccounInfo);
  const getUserName = async () => {
    try {
      const response = await userApi.fetchUser();
      setUserName(response.data.name);
    } catch (error) {
      console.log("user의 fetchUser : ", error);
    }
  };

  const getInvitationCode = async () => {
    try {
      if (meetingAccountInfo.groupId !== undefined) {
        const response = await accountApi.fetchInvitationCode(meetingAccountInfo.groupId);
        setGroupCode(response.data.groupCode);
      }
    } catch (error) {
      console.log("account의 fetchInvitationCode : ", error);
    }
  };

  const shareKakao = () => {
    getUserName();
    getInvitationCode();
  };

  const sendKakao = () => {
    window.Kakao.Link.sendCustom({
      templateId: 112239,
      templateArgs: {
        groupLeader: userName,
        groupName: meetingAccountInfo.groupName,
        code: groupCode,
      },
    });
  };

  const handleNext = () => {
    if (params.type === "meeting") {
      navigate("/travelbox/create/prepare");
    } else {
      navigate("/travelbox/create/type", {
        state: {
          currencyCode: location.state.currencyCode,
          groupId: location.state.groupId,
        },
      });
    }
  };

  useEffect(() => {
    if (userName !== "" && groupCode !== "") {
      sendKakao();
    }
  }, [groupCode]);

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl flex flex-col justify-center">
            {params.type === "travelbox" ? (
              <div className="flex">
                <p className="text-[#1429A0] font-semibold">외화저금통</p>
                <p>이</p>
              </div>
            ) : (
              <div className="flex">
                <p>튜나뱅크&nbsp;</p>
                <p className="text-[#1429A0] font-semibold">{params.type === "meeting" ? "모임통장" : "입출금통장"}</p>
                <p>이</p>
              </div>
            )}
            <p className="text-center">개설되었어요</p>
          </div>
        </div>

        <div className="flex flex-col space-y-3">
          {params.type === "meeting" ? (
            <button
              className="w-full h-14 text-[#565656] bg-[#FAE100] rounded-lg flex justify-center items-center space-x-2"
              onClick={() => (userName !== "" && groupCode !== "" ? sendKakao() : shareKakao())}>
              <img
                className="w-6 aspect-1"
                src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"
                alt="카카오링크 보내기 버튼"
              />
              <p>카카오톡으로 초대장 공유하기</p>
            </button>
          ) : (
            <></>
          )}

          <button
            onClick={() => {
              handleNext();
            }}
            className={`w-full h-14 text-lg rounded-xl tracking-wide ${
              params.type === "meeting" ? "" : "text-white bg-[#1429A0]"
            }`}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default CompletedOfCreateMeetingAccount;
