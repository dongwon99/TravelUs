import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { accountApi } from "../../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { IoMdClose } from "react-icons/io";

const PasswordOfCreateCard = () => {
  const navigate = useNavigate();
  const { groupId, type } = useParams();
  const [accountNo, setAccountNo] = useState("");
  const [password, setPassword] = useState("");

  // 계좌 비밀번호 검증
  const checkPassword = async () => {
    const data = {
      accountNo: accountNo,
      accountPassword: password,
    };

    try {
      const response = await accountApi.fetchCheckAccountPassword(data);
      if (response.status === 200) {
        setPassword("");
        navigate(`/card/${groupId}/create/password/card`);
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("모임통장 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.log("", error);
    }
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        setAccountNo(response.data.groupAccountNo);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, []);

  useEffect(() => {
    if (password.length === 4) {
      if (type === "meeting" && accountNo !== "") {
        checkPassword();
      } else {
        navigate(`/card/${groupId}/create/password/check`, { state: { originalPassword: password } });
      }
    }
  }, [accountNo, password]);

  return (
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={() => {
          navigate(`/card/${groupId}/create/englishname`);
        }}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {type === "meeting" ? "카드를 개설할 모임통장의" : "모임 카드에서 사용할"}
          <br />
          비밀번호를 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${index < password.length ? "bg-[#565656]" : "bg-[#D9D9D9]"} rounded-full`}
              key={index}></div>
          ))}
        </div>
      </div>
      <SecurityNumberKeyboard password={password} setPassword={setPassword} />
    </div>
  );
};

export default PasswordOfCreateCard;
