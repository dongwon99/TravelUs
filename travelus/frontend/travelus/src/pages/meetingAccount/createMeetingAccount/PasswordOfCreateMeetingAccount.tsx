import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { useLocation, useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { accountApi } from "../../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { exchangeRateApi } from "../../../api/exchange";
import { setMeetingAccountInfo } from "../../../redux/meetingAccountSlice";
import { IoMdClose } from "react-icons/io";

const PasswordOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const { type } = useParams();
  const [password, setPassword] = useState("");
  const travelboxInfo = useSelector((state: RootState) => state.meetingAccount.travelboxInfo);
  const exchangeTargetInfo = useSelector((state: RootState) => state.meetingAccount.exchangeTargetInfo);
  const meetingAccountInfo = useSelector((state: RootState) => state.meetingAccount.meetingAccounInfo);

  const createTravelbox = async () => {
    const travelboxData = {
      accountPassword: password,
      accountNo: travelboxInfo.accountNo,
      currencyCode: travelboxInfo.currencyCode,
    };
    try {
      const response = await accountApi.fetchCreateTravelBox(travelboxData);
      const updatedMeetingAccountInfo = { ...meetingAccountInfo, groupId: response.data.groupId };
      dispatch(setMeetingAccountInfo(updatedMeetingAccountInfo));
      if (response.status === 201) {
        navigate("/meeting/create/completed/travelbox", {
          state: {
            currencyCode: travelboxInfo.currencyCode,
            accountNo: travelboxInfo.accountNo,
            groupId: response.data.groupId,
          },
        });
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.log("accountApi의 fetchCreateTravelBox : ", error);
    }
  };

  const createTargetRate = async () => {
    const targetRate = {
      groupId: Number(meetingAccountInfo.groupId),
      currencyCode: travelboxInfo.currencyCode,
      transactionBalance: exchangeTargetInfo.transactionBalance,
      targetRate: exchangeTargetInfo.targetRate,
      dueDate: "2024-10-30",
      accountPassword: password,
    };

    try {
      if (location.state.exchangeType === "AUTO") {
        // 사용자 설정 자동환전 설정이 수정인 경우
        const response = await accountApi.fetchChangeAutoExchangeInfo(targetRate);
        if (response.status === 200) {
          navigate("/travelbox/create/auto/exchange/completed/AUTO", {
            state: { nextPath: `/meetingaccount/${meetingAccountInfo.groupId}` },
          });
        }
      } else {
        // 사용자 설정 자동환전 설정이 첫 등록인 경우
        const response = await exchangeRateApi.postExchangeTargetRate(targetRate);
        if (response.status === 200) {
          navigate("/travelbox/create/auto/exchange/completed/AUTO", {
            state: { nextPath: `/meetingaccount/${meetingAccountInfo.groupId}` },
          });
        }
      }
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "ACCOUNT_PASSWORD_INVALID") {
          setPassword("");
          alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
      }
      console.log("exchangeRateApi의 postExchangeTargetRate : ", error);
    }
  };

  const changeExchangeMode = async () => {
    if (meetingAccountInfo.groupId === undefined) return;

    const data = {
      groupId: meetingAccountInfo.groupId,
      exchangeType: "AUTO",
    };

    try {
      const response = await accountApi.fetchChangeExchangeMode(data);
      if (response.status === 200) {
      }
    } catch (error) {
      console.log("accountApi의 fetchChangeExchangeMode : ", error);
    }
  };

  useEffect(() => {
    if (password.length === 4) {
      if (type === "travelbox") {
        createTravelbox();
      } else if (type === "exchangeSetting") {
        createTargetRate();
        changeExchangeMode();
      } else {
        navigate("/meeting/create/password/check", { state: { originalPassword: password, type: type } });
      }
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={() => {
          navigate("/");
        }}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          {type === "travelbox" || type === "exchangeSetting"
            ? "모임통장의 비밀번호를"
            : type === "meeting"
            ? "모임통장에서 사용할"
            : "튜나뱅크에서 사용할"}
          <br />
          {type === "travelbox" ? "입력해주세요" : "비밀번호를 입력해주세요"}
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

export default PasswordOfCreateMeetingAccount;
