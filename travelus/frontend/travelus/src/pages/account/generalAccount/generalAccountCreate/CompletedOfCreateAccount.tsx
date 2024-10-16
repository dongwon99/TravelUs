import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { userApi } from "../../../../api/user";
import { accountApi } from "../../../../api/account";
import { useSelector } from "react-redux";
import { RootState } from "../../../../redux/store";

const CompletedOfCreateAccount = () => {
  const navigate = useNavigate();

  const handleNext = () => {
    navigate("/");
  };

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />

          <div className="text-2xl flex flex-col justify-center">
            <div className="flex">
              <p>튜나뱅크&nbsp;</p>
              <p className="text-[#1429A0] font-semibold">입출금통장</p>
              <p>이</p>
            </div>

            <p className="text-center">개설되었어요</p>
          </div>
        </div>

        <div className="flex flex-col space-y-3">
          <button
            onClick={() => {
              handleNext();
            }}
            className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
            >
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default CompletedOfCreateAccount;
