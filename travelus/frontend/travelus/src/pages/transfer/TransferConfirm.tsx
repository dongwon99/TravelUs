import React, { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { accountApi } from "../../api/account";
import { userApi } from "../../api/user";
import { AccountInfoNew } from "../../types/account";
import { IoIosArrowBack } from "react-icons/io";
import Loading from "../../components/loading/Loading";

interface TransferConfirmProps {
  // Define the props for the component here
}

const TransferConfirm: React.FC<TransferConfirmProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isValidation, setIsValidation] = useState<boolean>(false);
  const [withdrawalAccountNo, setWithdrawalAccountNo] = useState<string>("");
  const [userName, setUserName] = useState<string>("");
  const { accountNo, transferAmount, depositAccount } = location.state as {
    accountNo: string;
    transferAmount: string;
    password: string;
    depositAccount: string;
  };

  const formatAccountNumber = (accountNo: string) => {
    if (accountNo.length !== 14) {
      return accountNo;
    }
    // 계좌번호를 3-8-2 형식으로 변환
    return `${accountNo.slice(0, 3)}-${accountNo.slice(3, 11)}-${accountNo.slice(11)}`;
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 출금 계좌 번호를 위한 API 호출
        const accountResponse = await accountApi.fetchAllAccountInfo("I");
        setWithdrawalAccountNo(accountResponse.data[0].accountNo);

        // 사용자 이름을 위한 API 호출
        const userResponse = await userApi.fetchUser();
        setUserName(userResponse.data.name);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  if (!userName || !withdrawalAccountNo) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="h-full flex flex-col justify-between">
        <div>
          <IoIosArrowBack
            onClick={() => {
              navigate("/transfer/selectbank");
            }}
            className="text-2xl"
          />
        </div>
        <div className="mb-16 flex flex-col items-center">
          <p className="text-2xl font-bold">
            {depositAccount}
            <span className="font-normal"> 님에게</span>
          </p>
          <p className="text-2xl font-bold">{formatCurrency(parseInt(transferAmount))}원</p>
          <p className="text-2xl">보낼까요?</p>
        </div>
        <div className="flex flex-col space-y-6">
          <div className="flex flex-col space-y-2">
            <div className="flex justify-between">
              <p className="text-zinc-500">받는 분에게 표시</p>
              <p>{userName}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">출금 계좌</p>
              <p>{formatAccountNumber(withdrawalAccountNo)}</p>
            </div>
            <div className="flex justify-between">
              <p className="text-zinc-500">입금 계좌</p>
              <p>{formatAccountNumber(accountNo)}</p>
            </div>
          </div>
          <div className="flex flex-col space-y-3">
            <button
              onClick={() => {
                navigate("/transfer/password", {
                  state: { accountNo, transferAmount, depositAccount, userName, withdrawalAccountNo },
                });
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide text-white bg-[#1429A0]">
              보내기
            </button>
            <button
              onClick={() => {
                navigate("/transfer/selectbank");
              }}
              className="w-full h-14 text-lg font-semibold rounded-xl tracking-wide">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransferConfirm;
