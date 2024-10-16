import React, { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useLocation } from "react-router";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { setAccountPassword } from "../../redux/accountSlice";
import { exchangeRateApi } from "../../api/exchange";
import { ExchangeRequest, ExchangeResponse } from "../../types/exchange";
import { IoMdClose } from "react-icons/io";

interface LocationState {
  accountNo: string;
  sourceCurrencyCode: string;
  targetCurrencyCode: string;
  transactionBalance: number;
  groupId: number;
}

const AccountPasswordInput = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const [stateData, setStateData] = useState<LocationState | null>(null);
  const [password, setPassword] = useState<string>("");

  const formatNumber = (num: number | string | undefined) => {
    return Number(num).toLocaleString();
  };

  const getCurrencyUnit = (code: string | undefined) => {
    return code === "KRW" ? "원" : code;
  };

  useEffect(() => {
    if (location.state) {
      setStateData(location.state as LocationState);
    } else {
      console.log("No State received");
      navigate(-1);
    }

    return () => {
      dispatch(setAccountPassword(""));
    };
  }, [location, dispatch, navigate]);

  useEffect(() => {
    if (password.length === 4 && stateData) {
      handleExchange();
    }
  }, [password, stateData]);

  const handleExchange = async () => {
    if (!stateData) return;

    const exchangeRequest: ExchangeRequest = {
      transferType: "M",
      accountNo: stateData.accountNo,
      accountPassword: password,
      sourceCurrencyCode: stateData.sourceCurrencyCode,
      targetCurrencyCode: stateData.targetCurrencyCode,
      transactionBalance: stateData.transactionBalance,
    };

    console.log(exchangeRequest);

    try {
      const response: ExchangeResponse[] = await exchangeRateApi.requestExchange(exchangeRequest);

      const targetTransactionAmount = response[1].transactionAmount;
      const exchangeSummary = response[1].transactionSummary;

      navigate("/exchange/exchange-completion", {
        state: {
          sourceCurrencyCode: stateData.sourceCurrencyCode,
          targetCurrencyCode: stateData.targetCurrencyCode,
          sourceAmount: stateData.transactionBalance,
          targetAmount: targetTransactionAmount,
          transactionSummary: exchangeSummary,
          groupId: stateData.groupId,
        },
      });
    } catch (error: any) {
      console.log("Exchange failed:", error);
      if (error.response && error.response.data) {
        const { errorMessage } = error.response.data;
        if (errorMessage === `AccountPassword Does Not Match: ${password}`) {
          alert("계좌 비밀번호가 일치하지 않습니다.");
        } else if (errorMessage === "amount is below the minimum allowable amount") {
          alert("최소 환전 금액이 아닙니다.");
        } else {
          alert(errorMessage || "알 수 없는 오류가 발생했습니다.");
        }
      } else {
        alert("알 수 없는 오류가 발생했습니다.");
      }
      setPassword(""); // 오류 발생 시 비밀번호 초기화
    } finally {
      dispatch(setAccountPassword(""));
    }
  };

  return (
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={() => {
          navigate("/");
        }}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
      <div className="flex-grow flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {formatNumber(stateData?.transactionBalance)}
          {getCurrencyUnit(stateData?.sourceCurrencyCode)}을 환전하려면
          <br />
          계좌 비밀번호를 입력해주세요
        </p>
        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${index < password.length ? "bg-[#565656]" : "bg-[#d9d9d9]"} rounded-full`}
              key={index}></div>
          ))}
        </div>
      </div>
      <div>
        <SecurityNumberKeyboard password={password} setPassword={setPassword} />
      </div>
    </div>
  );
};

export default AccountPasswordInput;
