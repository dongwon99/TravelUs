import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { setExchangeTargetRate } from "../../redux/meetingAccountSlice";
import { RootState } from "../../redux/store";
import { IoIosArrowBack } from "react-icons/io";
import ExchangeAmmountInput from "../../components/travelBox/ExchangeAmmountInput";

const AutoCurrencyExchangeAmountOfCreateTravelBox = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const [exchangeAmount, setExchangeAmount] = useState(0);
  const exchangeTargetInfo = useSelector((state: RootState) => state.meetingAccount.exchangeTargetInfo);

  const handleNext = () => {
    dispatch(setExchangeTargetRate({ ...exchangeTargetInfo, transactionBalance: exchangeAmount }));
    navigate("/meeting/create/password/exchangeSetting", { state: { exchangeType: location.state.exchangeType } });
  };

  return (
    <div className="h-full">
      <div>
        <div className="p-5 bg-white flex items-center sticky top-0">
          <IoIosArrowBack onClick={() => navigate(-1)} className="text-2xl" />
        </div>

        <div className="p-5 pb-32 grid gap-10 overflow-y-auto">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">03</p>
              <p className="font-medium">자동환전 금액 설정</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>얼마를</p>
              <p>환전할까요?</p>
            </div>
          </div>

          <ExchangeAmmountInput exchangeAmount={exchangeAmount} setExchangeAmount={setExchangeAmount} />
        </div>
      </div>

      <div className="w-full p-5 pb-8 bg-white fixed bottom-0 z-50">
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => handleNext()}>
          다음
        </button>
      </div>
    </div>
  );
};

export default AutoCurrencyExchangeAmountOfCreateTravelBox;
