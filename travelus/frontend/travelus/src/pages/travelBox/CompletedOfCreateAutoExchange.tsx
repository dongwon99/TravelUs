import React from "react";
import { useLocation, useNavigate, useParams } from "react-router";

const CompletedOfCreateAutoExchange = () => {
  const { type } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl flex flex-col justify-center">
            <div className="flex justify-center">
              {type === "NOW" ? (
                <>
                  <p className="text-[#1429A0] font-semibold">즉시 자동환전</p>
                  <p>으로</p>
                </>
              ) : (
                <></>
              )}
              {type === "NONE" ? (
                <>
                  <p className="text-[#1429A0] font-semibold">자동환전</p>
                  <p>을</p>
                </>
              ) : (
                <></>
              )}
              {type === "AUTO" ? (
                <>
                  <p className="text-[#1429A0] font-semibold">사용자 설정 자동환전</p>
                  <p>으로</p>
                </>
              ) : (
                <></>
              )}
            </div>

            {type === "NOW" ? <p className="text-center">설정되었어요</p> : <></>}
            {type === "NONE" ? <p className="text-center">설정하지 않았어요</p> : <></>}
            {type === "AUTO" ? <p className="text-center">설정되었어요</p> : <></>}
          </div>
        </div>

        <button
          className={`w-full h-14 text-lg rounded-xl tracking-wide ${
            type === "meeting" ? "" : "text-white bg-[#1429A0]"
          }`}
          onClick={() => navigate(location.state.nextPath)}>
          확인
        </button>
      </div>
    </div>
  );
};

export default CompletedOfCreateAutoExchange;
