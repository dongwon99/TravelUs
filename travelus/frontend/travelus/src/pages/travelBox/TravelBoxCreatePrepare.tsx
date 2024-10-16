import React from "react";
import { useLocation, useNavigate } from "react-router";

const TravelBoxCreatePrepare = () => {
  const navigate = useNavigate();

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="h-full text-2xl font-semibold tracking-wider flex flex-col justify-center items-center">
        <div className="flex">
          <p>이어서,&nbsp;</p>
          <p className="text-[#1429A0]">외화저금통</p>
        </div>
        <p className="text-[#1429A0]">개설을 시작할게요</p>
      </div>

      <div>
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => navigate("/travelbox/create/currency")}>
          개설하기
        </button>
        <button className="w-full h-14 text-lg rounded-xl tracking-wide" onClick={() => navigate("/")}>
          건너뛰기
        </button>
      </div>
    </div>
  );
};

export default TravelBoxCreatePrepare;
