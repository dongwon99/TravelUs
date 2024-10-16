import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { useNavigate, useLocation, useParams } from "react-router";
import { transactionApi } from "../../../api/transaction";
import { userApi } from "../../../api/user";
import { accountApi } from "../../../api/account";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { MeetingAccountDetailInfo } from "../../../types/account";

const DeleteTravelBoxPassword: React.FC = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { groupId } = useParams();
  const { accountNo } = useParams();

  const [password, setPassword] = useState("");
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);

  useEffect(() => {
    if (password.length === 4) {
      handleDelete();
    }
  }, [password]);

  useEffect(() => {
    if (!accountNo) return;

    fetchSpecificAccountInfo(accountNo);
  }, [accountNo]);

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  const handleDelete = async () => {
    if (!accountNo || !account) {
      return;
    }

    try {
      const response = await accountApi.fetchDeleteTravelBox(
        password,
        accountNo,
        account?.moneyBoxDtos[1].currencyCode
      );
      navigate(`/travelbox/delete/${accountNo}/${groupId}/completed`);
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.error("외화저금통 해지 에러", error);
    }
  };

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          <br />
          모임통장 비밀번호를 입력해주세요
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

export default DeleteTravelBoxPassword;
