import React, { useEffect, useState } from "react";
import SecurityNumberKeyboard from "../../../../components/common/SecurityNumberKeyboard";
import { useLocation, useNavigate } from "react-router";
import { settlementApi } from "../../../../api/settle";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../../types/axiosError";
import Loading from "../../../../components/loading/Loading";

const SettlementTransferPassword = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (password.length === 4) {
      handleTransfer();
    }
  }, [password]);

  const handleTransfer = async () => {
    const settlement = location.state.data;
    const data = {
      settlementDetailId: settlement.settlementDetailId,
      withdrawalAccountNo: settlement.withdrawalAccountNo,
      depositAccountNo: settlement.depositAccountNo,
      transactionBalance: Number(settlement.transactionBalance),
      withdrawalTransactionSummary: settlement.withdrawalTransactionSummary,
      accountPassword: password,
      depositTransactionSummary: settlement.depositTransactionSummary,
    };

    try {
      setIsLoading(true);
      const response = await settlementApi.fetchSettlementPersonalTransfer(data);
      if (response.status === 200) {
        navigate("/settlement/expenditure/transfer/success", {
          state: { transferAmount: data.transactionBalance, depositAccountName: data.withdrawalTransactionSummary },
        });
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.error("settlementApi의 fetchSettlementPersonalTransfer : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          <br />
          비밀번호를 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${
                index < password.length ? "bg-[#565656]" : "bg-[#D9D9D9]"
              } rounded-full`}></div>
          ))}
        </div>
      </div>

      <SecurityNumberKeyboard password={password} setPassword={setPassword} />
    </div>
  );
};

export default SettlementTransferPassword;
