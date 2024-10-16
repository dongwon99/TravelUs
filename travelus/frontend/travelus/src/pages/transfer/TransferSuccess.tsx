import React from "react";
import { useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { AccountInfoNew } from "../../types/account";
import { IoIosArrowBack } from "react-icons/io";

const TransferSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { transferAmount } = location.state as { transferAmount: string };
  const { depositAccount } = location.state as { depositAccount: string };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="h-full mt-32 flex flex-col items-center space-y-5">
          <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />
          <div className="text-2xl font-semibold text-center">
            <p>
              {depositAccount}
              <span className="font-normal"> 님에게</span>
            </p>
            <p>
              {formatCurrency(parseInt(transferAmount))}원<span className="font-normal">을</span>
            </p>
            <p className="font-normal">보냈어요</p>
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

export default TransferSuccess;
