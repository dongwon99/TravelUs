import React, { useState, useEffect } from "react";
import { BsDot } from "react-icons/bs";
import { currencyTypeList } from "../../types/exchange";

interface Props {
  currencyCode: string;
  exchangeRateFront: number;
  setExchangeRateFront: (num: number) => void;
  exchangeRateBack: number;
  setExchangeRateBack: (num: number) => void;
}

const ExchangeRateInputMui = ({
  currencyCode,
  exchangeRateFront,
  setExchangeRateFront,
  exchangeRateBack,
  setExchangeRateBack,
}: Props) => {
  const [isFocusedFront, setIsFocusedFront] = useState(false);
  const [isFocusedBack, setIsFocusedBack] = useState(false);
  const [displayFront, setDisplayFront] = useState("");

  const formatNumber = (num: number) => {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  };

  useEffect(() => {
    setDisplayFront(exchangeRateFront === 0 ? "" : formatNumber(exchangeRateFront));
  }, [exchangeRateFront]);

  const handleFrontChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/,/g, "");
    if (value === "" || /^\d+$/.test(value)) {
      setExchangeRateFront(Number(value));
    }
  };

  return (
    <div className="grid gap-3">
      <div className="flex justify-between">
        <label className="text-[#565656]" htmlFor="">
          희망 환율
        </label>
        <p>{currencyTypeList.find((item) => item.value === currencyCode)?.text}</p>
      </div>

      <div className="text-xl flex justify-between space-x-3">
        <div className="flex items-end space-x-2">
          <input
            className="w-32 text-right border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="text"
            placeholder="0"
            value={isFocusedFront && exchangeRateFront === 0 ? "" : displayFront}
            onChange={handleFrontChange}
            onFocus={() => setIsFocusedFront(true)}
            onBlur={() => {
              setIsFocusedFront(false);
              if (exchangeRateFront === 0) setExchangeRateFront(0);
            }}
          />
          <BsDot />
          <input
            className="w-16 text-right border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="number"
            placeholder="0"
            value={isFocusedBack && exchangeRateBack === 0 ? "" : exchangeRateBack}
            onChange={(e) => {
              const value = e.target.value;
              if (value.length <= 2) {
                setExchangeRateBack(Number(value));
              }
            }}
            onFocus={() => setIsFocusedBack(true)}
            onBlur={() => {
              setIsFocusedBack(false);
              if (exchangeRateBack === 0) setExchangeRateBack(0);
            }}
          />
        </div>

        <p className="text-[#1429A0] font-semibold">원 이하일 때</p>
      </div>
    </div>
  );
};

export default ExchangeRateInputMui;
