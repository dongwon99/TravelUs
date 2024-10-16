import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { FcMoneyTransfer } from "react-icons/fc";
import { ExchangeResponse } from "../../types/exchange";
import Loading from "../../components/loading/Loading";

interface LocationState {
  sourceCurrencyCode: string;
  targetCurrencyCode: string;
  sourceAmount: string;
  targetAmount: string;
  exchangeRate?: number;
  transactionSummary: string;
  groupId: number;
}

const currencyNameMapping: { [key: string]: string } = {
  KRW: "원",
  // USD: "달러",
  // JPY: "엔",
  // TWD: "대만 달러",
  // EUR: "유로",
};

// 숫자 포맷팅 함수
const formatNumber = (value: string, currencyCode: string): string => {
  const num = parseFloat(value);
  if (isNaN(num)) return value;

  if (currencyCode === "KRW") {
    // 원화의 경우 소수점 이하 숫자 제거 및 천 단위 쉼표 추가
    return Math.round(num).toLocaleString("ko-KR");
  } else {
    // 다른 통화의 경우 소수점 둘째 자리까지 표시 및 천 단위 쉼표 추가
    const formattedNum = num.toLocaleString("ko-KR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

    return formattedNum.replace(/\.00$/, "");
  }
};

const ExchangeCompletion: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as LocationState;
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!state) {
      navigate("/");
      return;
    }

    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000); // 1초 동안 로딩 화면 표시

    return () => clearTimeout(timer);
  }, [state, navigate]);

  if (isLoading) {
    return <Loading />;
  }

  if (!state) {
    return null;
  }

  const { sourceCurrencyCode, targetCurrencyCode, sourceAmount, targetAmount, transactionSummary, groupId } = state;
  console.log(groupId);
  const extractExchangeRate = (summary: string): string => {
    const match = summary.match(/(\d+(\.\d+)?)/);
    return match ? match[1] : "N/A";
  };

  const extractRate = extractExchangeRate(transactionSummary);

  const getLocalCurrencyName = (currencyCode: string): string => {
    return currencyNameMapping[currencyCode] || currencyCode;
  };

  const handleClose = () => {
    // navigate("/");
    // 모임통장 상세 페이지로 이동
    navigate(`/meetingaccount/${groupId}`);
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col">
      <div className="flex-grow flex flex-col items-center justify-center space-y-5">
        <img className="w-20 aspect-1" src="/assets/confirmIcon.png" alt="확인아이콘" />
        <div className="text-2xl font-semibold text-center">
          <p>
            {formatNumber(targetAmount, targetCurrencyCode)} {getLocalCurrencyName(targetCurrencyCode)}
          </p>
          <p className="font-normal">채우기 완료</p>
        </div>
      </div>

      <div className="mb-6">
        <div className="mb-4 flex justify-between">
          <p className="text-gray-500 text-sm">환전 금액</p>
          <p className="text-lg">
            {formatNumber(sourceAmount, sourceCurrencyCode)} {getLocalCurrencyName(sourceCurrencyCode)} →{" "}
            {formatNumber(targetAmount, targetCurrencyCode)} {getLocalCurrencyName(targetCurrencyCode)}
          </p>
        </div>

        <div className="flex justify-between">
          <p className="text-gray-500 text-sm mb-1">적용 환율</p>
          <p className="text-lg">{extractRate} 원</p>
        </div>
      </div>

      <button onClick={handleClose} className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]">
        닫기
      </button>
    </div>
  );
};

export default ExchangeCompletion;
