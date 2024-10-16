import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { RiErrorWarningFill } from "react-icons/ri";
import { RiExchangeDollarLine } from "react-icons/ri";
import { MeetingAccountDetailInfo, MeetingAccountInfo } from "../../../types/account";
import { accountApi } from "../../../api/account";
import { currencyTypeList } from "../../../types/exchange";
import Loading from "../../../components/loading/Loading";
import { tr } from "date-fns/locale";

const DeleteTravelBox = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const { accountNo } = useParams();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);
  const [isvalid, setIsValid] = useState<boolean>(false);

  useEffect(() => {
    if (!accountNo) return;

    fetchSpecificAccountInfo(accountNo);
  }, [accountNo]);

  useEffect(() => {
    // 해당 모임 조회 API 호출
    const fetchSpecificMeetingAccount = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          setMeeting(response.data);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchSpecificMeetingAccount();
  }, [groupId]);

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
      if (response.data.moneyBoxDtos[1].balance === 0) {
        setIsValid(true);
      }
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const handleNext = () => {
    navigate(`/travelbox/delete/${accountNo}/${groupId}/password`);
  };

  if (!account || !meeting) {
    return <Loading />;
  }

  return (
    <div className="min-h-screen h-full flex flex-col">
      <div className="h-full flex flex-col justify-between">
        <div>
          <div className="p-5 flex flex-col space-y-5">
            <IoIosArrowBack
              onClick={() => {
                navigate(`/meetingaccount/management/${groupId}`);
              }}
              className="text-2xl"
            />
            <p className="font-bold text-xl">해지할 외화저금통을 확인해주세요</p>
          </div>

          <div className="p-5 space-y-5">
            <div className="">
              <p className="text-zinc-500">모임통장 정보</p>
              <p className="text-lg font-semibold">{meeting.groupName}</p>
              <p className="text-zinc-600 font-semibold">{meeting.groupAccountNo}</p>
            </div>

            <div className="">
              <p className="text-zinc-500">외화저금통 잔액</p>
              <div className="flex space-x-2 items-center">
                <div className="flex space-x-1">
                  <p className="text-lg font-semibold">{formatCurrency(account.moneyBoxDtos[1].balance)}</p>
                  <p className="text-lg font-semibold">
                    {currencyTypeList
                      .find((item) => item.value === account.moneyBoxDtos[1].currencyCode)
                      ?.text.slice(-2, -1)}
                  </p>
                </div>
                <p className="text-lg font-semibold">
                  {currencyTypeList.find((item) => item.value === account.moneyBoxDtos[1].currencyCode)?.text.slice(3)}
                </p>
              </div>
            </div>

            {account.moneyBoxDtos[1].balance > 0 && (
              <div className="flex items-center space-x-1">
                <RiErrorWarningFill className="text-[#F2B91E]" />
                <p className="text-xs text-red-600 font-semibold">외화저금통에 잔액이 있을 경우 해지할 수 없어요</p>
              </div>
            )}
          </div>

          <div className="w-full h-5 bg-[#F6F6F8]"></div>

          {account.moneyBoxDtos[1].balance > 0 && (
            <div className="p-5 flex items-center space-x-1">
              <RiExchangeDollarLine className="text-lg text-[#27995a]" />
              <p className="font-semibold">먼저 재환전을 통해 외화저금통을 비워주세요</p>
            </div>
          )}
        </div>

        <div className="p-5 pb-8">
          <button
            onClick={handleNext}
            className={`w-full h-14 text-lg rounded-xl tracking-wide font-semibold ${
              isvalid ? "text-[#DD1F36] bg-[#FFE4E5]" : "text-[#565656] bg-[#E3E4E4]"
            }`}
            disabled={!isvalid}>
            {isvalid ? "해지하기" : "해지 불가"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteTravelBox;
