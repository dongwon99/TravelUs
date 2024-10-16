import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../redux/store";
import { useNavigate, useLocation, useParams } from "react-router";
import { transactionApi } from "../../../../api/transaction";
import { userApi } from "../../../../api/user";
import { accountApi } from "../../../../api/account";
import SecurityNumberKeyboard from "../../../../components/common/SecurityNumberKeyboard";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../../types/axiosError";
import { MeetingAccountInfo } from "../../../../types/account"
import { IoMdClose } from "react-icons/io";

interface TransferPasswordProps {
  // Define the props for your component here
}

const FillPassword: React.FC<TransferPasswordProps> = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { groupId } = useParams();

  const [password, setPassword] = useState("");
  const [userName, setUserName] = useState<string>("");
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const { transferAmount } = location.state as { transferAmount: string };
  const { meetingAccountNo } = location.state as { meetingAccountNo: string };
  const { generalAccountNo } = location.state as { generalAccountNo: string };

  useEffect(() => {
    if (password.length === 4) {
      handleTransfer();
    }
  }, [password]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 사용자 이름을 위한 API 호출
        const userResponse = await userApi.fetchUser();
        setUserName(userResponse.data.name);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    
    fetchData();
    fetchSpecificMeetingAccount();
  }, []);

  const formatAccountNumber = (accountNo: string) => {
    if (accountNo.length !== 14) {
      return accountNo;
    }
    // 계좌번호를 3-8-2 형식으로 변환
    return `${accountNo.slice(0, 3)}-${accountNo.slice(3, 11)}-${accountNo.slice(11)}`;
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  const handleTransfer = async () => {
    if (!meeting) {
      return;
    }

    const data = {
      transferType: "G",
      withdrawalAccountNo: formatAccountNumber(generalAccountNo),
      accountPassword: password,
      depositAccountNo: formatAccountNumber(meetingAccountNo),
      transactionBalance: parseInt(transferAmount),
      withdrawalTransactionSummary: meeting?.groupName,
      depositTransactionSummary: userName,
    };

    try {
      const response = await transactionApi.Transfer(data);
      navigate(`/meeting/${groupId}/fill/success`, { state: { transferAmount } });
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
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={() => {
          navigate(`/meeting/${groupId}/fill/setmoney`);
        }}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
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

export default FillPassword;
