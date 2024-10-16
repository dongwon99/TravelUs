import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import AccountNumberInput from "../../components/transfer/inputFields/AccountNumberInput";
import { accountApi } from "../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../types/axiosError";
import AccountTypeInput from "../../components/transfer/inputFields/AccountTypeInput";
import { format } from "path";

interface TransferSelectBankProps {
  // Define the props for your component here
}

const TransferSelectBank = () => {
  const navigate = useNavigate();

  const [accountNo, setAccountNo] = useState<string>("");
  const [isValidation, setIsValidation] = useState<boolean>(false);

  const handleAccountNoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAccountNo(e.target.value);
  };

  useEffect(() => {
    if (accountNo.length > 0) {
      setIsValidation(true);
    } else {
      setIsValidation(false);
    }
  }, [accountNo]);

  const formatAccountNumber = (accountNo: string) => {
    if (accountNo.length !== 14) {
      return accountNo;
    }
    // 계좌번호를 3-8-2 형식으로 변환
    return `${accountNo.slice(0, 3)}-${accountNo.slice(3, 11)}-${accountNo.slice(11)}`;
  };

  const handleNext = async () => {
    const formattedAccountNo = formatAccountNumber(accountNo);
    try {
      console.log(formattedAccountNo);
      const response = await accountApi.fetchSpecificAccountInfo(formattedAccountNo);
      if (response.status === 201) {
        navigate("/transfer/setmoney", { state: { accountNo : formattedAccountNo } });
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_NO_INVALID") {
          alert("계좌번호가 존재하지 않습니다. 다시 입력해주세요.");
          setAccountNo("");
        }
      }
    }
  };

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div className="flex flex-col space-y-10">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
          <div className="flex flex-col space-y-16">
            <p className="text-2xl font-bold">어디로 돈을 보낼까요?</p>

            <div className="flex flex-col space-y-10">
              <AccountNumberInput value={accountNo} labelName="계좌 번호" onChange={handleAccountNoChange} />
              <AccountTypeInput />
            </div>
          </div>
        </div>

        <div>
          <button
            onClick={() => handleNext()}
            className={`w-full h-14 text-lg font-semibold rounded-xl tracking-wide ${
              isValidation ? "text-white bg-[#1429A0]" : "text-[#565656] bg-[#E3E4E4]"
            }`}
            disabled={!isValidation}>
            다음
          </button>
        </div>
      </div>
    </div>
  );
};

export default TransferSelectBank;
