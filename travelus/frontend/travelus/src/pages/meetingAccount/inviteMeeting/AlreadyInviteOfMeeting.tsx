import React, { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import Lottie from "lottie-react";
import invitationAnimation from "../../../lottie/invitationAnimation.json";

const AlreadyInviteOfMeeting = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="mt-32 flex flex-col items-center space-y-3">
        <Lottie className="w-36" animationData={invitationAnimation} />
        <div className="text-2xl text-center font-semibold">
          <p>{location.state.groupInfo.groupLeader}님의 모임통장에</p>
          <p>이미 참여했어요!</p>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => navigate("/meetingaccountlist")}>
        모임통장 바로가기
      </button>
    </div>
  );
};
export default AlreadyInviteOfMeeting;
