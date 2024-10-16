import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import Lottie from "lottie-react";
import invitationAnimation from "../../../lottie/invitationAnimation.json";
import { useLocation, useNavigate, useParams } from "react-router";

const InvitationOfMeeting = () => {
  const navigate = useNavigate();
  const { code } = useParams();
  const location = useLocation();
  const accessToken = localStorage.getItem("accessToken");
  const queryParams = new URLSearchParams(location.search);
  const groupInfo = {
    groupLeader: queryParams.get("groupLeader"),
    groupName: queryParams.get("groupName"),
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="mt-32 flex flex-col items-center space-y-3">
        <Lottie className="w-36" animationData={invitationAnimation} />
        <div className="text-2xl text-center font-semibold">
          <p>{groupInfo.groupLeader}님의 모임통장</p>
          <p>초대장 도착!</p>
        </div>
      </div>

      {accessToken ? (
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() =>
            navigate(`/meeting/invite/${code}/info`, {
              state: { groupInfo: groupInfo },
            })
          }>
          초대 수락하기
        </button>
      ) : (
        <div className="grid gap-3">
          <button
            className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
            onClick={() => navigate(`/login`, { state: { type: "invite", code: code, groupInfo: groupInfo } })}>
            로그인
          </button>
          <p className="text-[#565656] text-center">초대 수락 전, 로그인을 먼저 해주세요</p>
        </div>
      )}
    </div>
  );
};

export default InvitationOfMeeting;
