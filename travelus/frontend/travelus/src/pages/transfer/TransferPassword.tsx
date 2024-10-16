import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { useNavigate, useLocation } from "react-router";
import { transactionApi } from "../../api/transaction";
import { AccountInfoNew } from "../../types/account";
import SecurityNumberKeyboard from "../../components/common/SecurityNumberKeyboard";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../types/axiosError";

interface TransferPasswordProps {
  // Define the props for your component here
}

const TransferPassword: React.FC<TransferPasswordProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();

  const [password, setPassword] = useState("");
  const { accountNo } = location.state as { accountNo: string };
  const { transferAmount } = location.state as { transferAmount: string };
  const { depositAccount } = location.state as { depositAccount: string };
  const { userName } = location.state as { userName: string };
  const { withdrawalAccountNo } = location.state as { withdrawalAccountNo: string };

  useEffect(() => {
    if (password.length === 4) {
      handleTransfer();
    }
  }, [password]);

  const formatAccountNumber = (accountNo: string) => {
    if (accountNo.length !== 14) {
      return accountNo;
    }
    // 계좌번호를 3-8-2 형식으로 변환
    return `${accountNo.slice(0, 3)}-${accountNo.slice(3, 11)}-${accountNo.slice(11)}`;
  };

  const handleTransfer = async () => {
    const data = {
      transferType: "G",
      withdrawalAccountNo: formatAccountNumber(withdrawalAccountNo),
      accountPassword: password,
      depositAccountNo: formatAccountNumber(accountNo),
      transactionBalance: parseInt(transferAmount),
      withdrawalTransactionSummary: depositAccount,
      depositTransactionSummary: userName,
    };

    try {
      const response = await transactionApi.Transfer(data);
      console.log("이체 성공");
      navigate("/transfer/success", { state: { transferAmount, depositAccount } });
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.error("이체 에러", error);
    }
  };

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

export default TransferPassword;
