import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import Loading from "../../../components/loading/Loading";

const ExpenditureSettlementCompleted = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1500); // 1.5초 동안 로딩 화면 표시

    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl text-center">
            <div className="flex justify-center">
              <p className="text-[#1429A0] font-semibold">개별 지출 정산</p>
              <p>요청이</p>
            </div>
            <p>완료되었어요</p>
          </div>
        </div>

        <div className="flex flex-col space-y-6">
          <button
            onClick={() => {
              navigate("/");
            }}
            className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default ExpenditureSettlementCompleted;
