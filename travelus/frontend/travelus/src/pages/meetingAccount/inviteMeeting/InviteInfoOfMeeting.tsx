import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate, useParams } from "react-router";
import AccountListInputMui from "../../../components/meetingAccount/AccountListInputMui";
import { AiTwotoneExclamationCircle } from "react-icons/ai";
import { LuDot } from "react-icons/lu";
import { accountApi } from "../../../api/account";
import { AxiosError } from "axios";
import { AxiosErrorResponseData } from "../../../types/axiosError";
import { userApi } from "../../../api/user";
import { ParticipantInfo } from "../../../types/account";
import Loading from "../../../components/loading/Loading";

const InviteInfoOfMeeting = () => {
  const navigate = useNavigate();
  const { code } = useParams();
  const location = useLocation();
  const noticeTextList = [
    "모임통장 기능에는 정산하기 기능이 있습니다.",
    "정산 시, 선택한 입출금통장으로 정산금이 입금됩니다.",
    "초대 수락 시, 입출금통장을 보유해야 합니다.",
  ];
  const [userId, setUserId] = useState(0);
  const [groupId, setGroupId] = useState(0);
  const [accountNo, setAccountNo] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [accountList, setAccountList] = useState([]);

  const getUserInfo = async () => {
    try {
      const response = await userApi.fetchUser();
      setUserId(response.data.userId);
    } catch (error) {
      console.log(error);
    }
  };

  const getGroupId = async () => {
    if (code !== undefined) {
      try {
        setIsLoading(true);
        const response = await accountApi.fetchGroupIdByInvitationCode(code);
        setGroupId(response.data.groupId);

        // 이미 모임에 가입한 사용자인지 확인
        const isParticipate = response.data.participants.some((info: ParticipantInfo) => info.userId === userId);
        if (isParticipate) {
          // 이미 참여중 페이지로 이동
          navigate("/meeting/invite/participated", { state: { groupInfo: location.state.groupInfo } });
        }
      } catch (error) {
        const axiosError = error as AxiosError;
        if (axiosError.response && axiosError.response.data) {
          const responseData = axiosError.response.data as AxiosErrorResponseData;
          if (responseData.code === "601") {
            alert(responseData.message);
          }
        }
        console.log("account의 fetchGroupIdByInvitationCode", error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  const getAccountList = async () => {
    try {
      const response = await accountApi.fetchAllAccountInfo("I");
      setAccountList(response.data);
    } catch (error) {
      console.log("accountApi의 fetchAllAccountInfo : ", error);
    }
  };

  const handleInvitation = async () => {
    try {
      const data = {
        groupId: groupId,
        personalAccountNo: accountNo,
      };
      const response = await accountApi.fetchCreateParticipant(groupId, data);
      if (response.status === 201) {
        navigate("/meetingaccountlist");
      }
    } catch (error) {
      console.log("account의 fetchCreateParticipant", error);
    }
  };

  useEffect(() => {
    getUserInfo();
    getAccountList();
  }, []);

  useEffect(() => {
    getGroupId();
  }, [userId]);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <>
      {isLoading ? (
        <Loading />
      ) : (
        <div className="h-full p-5 pb-8 flex flex-col justify-between">
          <div className="grid gap-14">
            <div className="flex items-center">
              <IoIosArrowBack className="text-2xl" onClick={() => navigate(`/meeting/invite/${code}`)} />
            </div>

            <div className="grid gap-10">
              <div className="text-2xl font-semibold">
                <p>
                  정산금을 받을
                  <br />
                  입출금통장을 선택해주세요
                </p>
              </div>

              <AccountListInputMui accountNo={accountNo} setAccountNo={setAccountNo} accountList={accountList} />

              <div className="flex flex-col space-y-2">
                <div className="flex items-center space-x-1">
                  <AiTwotoneExclamationCircle />
                  <p>알아두세요</p>
                </div>

                {noticeTextList.map((text, index) => (
                  <div className="flex" key={index}>
                    <LuDot className="text-[#565656]" />
                    <p className="text-xs text-[#565656]">{text}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <button
            className={`w-full h-14 text-lg rounded-xl tracking-wide ${
              accountNo === "" ? "text-[#565656] bg-[#E3E4E4]" : "text-white bg-[#1429A0]"
            }`}
            onClick={() => handleInvitation()}>
            완료
          </button>
        </div>
      )}
    </>
  );
};

export default InviteInfoOfMeeting;
