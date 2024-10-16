import React, { useEffect, useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { MeetingAccountInfo, MeetingAccountDetailInfo } from "../../../types/account";
import { ParticipantInfo } from "../../../types/meetingAccount";
import { accountApi } from "../../../api/account";
import { userApi } from "../../../api/user";
import { IoIosArrowBack } from "react-icons/io";
import { IoPeopleSharp } from "react-icons/io5";
import { MdKeyboardArrowRight } from "react-icons/md";
import { IoCard } from "react-icons/io5";
import { AiFillSmile } from "react-icons/ai";
import { IoPerson } from "react-icons/io5";
import { IoIosListBox } from "react-icons/io";
import { RiHandCoinFill } from "react-icons/ri";
import { RiExchangeDollarLine } from "react-icons/ri";
import Loading from "../../../components/loading/Loading";
import { is } from "date-fns/locale";

interface MeetingAccountManagementProps {
  // Define the props for your component here
}

const MeetingAccountManagement: React.FC<MeetingAccountManagementProps> = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const numId = Number(id);
  const [isLoading, setIsLoading] = useState(false);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);
  const [exchangeType, setExchangeType] = useState<string | null>(null);
  const [memberList, setMemberList] = useState<ParticipantInfo[] | null>(null);
  const [isUserMaster, setIsUserMaster] = useState(false);
  const [userId, setUserId] = useState<number | null>(null);

  // 해당 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      setIsLoading(true);
      const response = await accountApi.fetchSpecificMeetingAccount(numId);
      if (response.status === 200) {
        setAccountInfo(response.data);
        setExchangeType(response.data.exchangeType);
        setMemberList(response.data.participants);
        fetchSpecificAccountInfo(response.data.groupAccountNo);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  // 모임 참여자가 userId로 본인인지 확인
  const fetchUser = async () => {
    try {
      const response = await userApi.fetchUser();
      if (response.status === 200) {
        const myId = response.data.userId;
        setUserId(myId);
      }
    } catch (error) {
      console.error("유저 정보 조회 에러", error);
    }
  };

  useEffect(() => {
    if (memberList && userId) {
      const currentUser = memberList.find((member) => member.userId === userId);
      setIsUserMaster(currentUser?.master || false);
    }
  }, [memberList, userId]);

  useEffect(() => {
    fetchSpecificMeetingAccount();
    fetchUser();
  }, [id]);

  if (isLoading || !account || !accountInfo || !memberList || !userId) {
    return <Loading />;
  }

  return (
    accountInfo && (
      <div className="h-full">
        <div className="p-6 pb-8 flex flex-col space-y-8">
          <div className="grid grid-cols-3 items-center">
            <IoIosArrowBack
              onClick={() => {
                navigate(`/meetingaccount/${numId}`);
              }}
              className="text-2xl"
            />
            <p className="text-lg text-center">관리</p>
          </div>

          <div className="flex flex-col space-y-2">
            <p className="text-lg font-semibold">{accountInfo?.groupName}</p>
            <p>계좌번호 · {accountInfo?.groupAccountNo}</p>
          </div>

          <div className="flex flex-col space-y-4">
            <div className="flex items-center space-x-4">
              <IoPeopleSharp className="text-3xl text-[#699BF7]" />
              <div className="w-full flex justify-between">
                <p className="text-zinc-500">모임원</p>
                <div
                  onClick={() => {
                    navigate(`/meetingaccount/management/${accountInfo?.groupId}/groupmember`);
                  }}
                  className="flex items-center">
                  <p>{accountInfo?.participants.length}명</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                </div>
              </div>
            </div>

            {!accountInfo.cardNumber ? (
              // 카드가 없을 경우 카드 발급하기로 연결
              <div
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/card/${accountInfo.groupId}/create/englishname`);
                }}
                className="flex items-center space-x-4">
                <IoCard className="text-3xl text-[#1429A0]" />
                <div className="w-full flex justify-between">
                  <p className="text-zinc-500">내 모임카드</p>
                  <div className="flex items-center">
                    <p className="text-[#0471E9]">발급하기</p>
                    <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                  </div>
                </div>
              </div>
            ) : (
              // 카드가 있을 경우 카드 상세 페이지로 연결
              <div
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/card/${accountInfo.groupId}`, { state: { groupId: accountInfo.groupId } });
                }}
                className="flex items-center space-x-4">
                <IoCard className="text-3xl text-[#1429A0]" />
                <div className="w-full flex justify-between">
                  <p className="text-zinc-500">내 모임카드</p>
                  <div className="flex items-center">
                    <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                  </div>
                </div>
              </div>
            )}

            <div
              onClick={() => {
                navigate(`/meetingaccount/update/${accountInfo.groupId}`);
              }}
              className="flex items-center space-x-4">
              <AiFillSmile className="text-3xl text-[#fcd876]" />
              <div className="w-full flex justify-between">
                <p className="text-zinc-500">아이콘 · 모임통장명</p>
                <div className="flex items-center">
                  <p className="text-[#0471E9]">변경하기</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                </div>
              </div>
            </div>

            {account.moneyBoxDtos.length > 1 && (
              <div
                onClick={() => {
                  exchangeType
                    ? navigate(`/travelbox/detail/auto/exchange/${id}`, {
                        state: { currencyCode: account.moneyBoxDtos[1].currencyCode, groupId: id },
                      })
                    : navigate("/travelbox/create/type", {
                        state: {
                          currencyCode: account.moneyBoxDtos[1].currencyCode,
                          groupId: id,
                          exchangeType: exchangeType,
                        },
                      });
                }}
                className="flex items-center space-x-4">
                <RiExchangeDollarLine className="text-3xl text-[#27995a]" />
                <div className="w-full flex justify-between">
                  <p className="text-zinc-500">자동환전</p>
                  <div className="flex items-center">
                    <p className="text-[#0471E9]">상세보기</p>
                    <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>

        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-6 pb-8 flex flex-col space-y-4">
          <p className="font-semibold">정산하기</p>
          <div className="flex flex-col space-y-4">
            {/* 모임장일 경우에만 정산하기 표시 */}
            {isUserMaster ? (
              <>
                <div className="flex items-center space-x-3">
                  <IoPerson className="text-3xl text-[#5e91ff]" />
                  <div className="w-full flex justify-between">
                    <p className="text-zinc-500">개별 지출 정산</p>
                    <div
                      onClick={() => {
                        navigate(`/settlement/expenditure/transaction/detail/${id}`);
                      }}
                      className="flex items-center">
                      <p className="text-[#0471E9]">정산하기</p>
                      <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                    </div>
                  </div>
                </div>
                <div className="flex items-center space-x-3">
                  <IoIosListBox className="text-3xl text-[#adb3be]" />
                  <div className="w-full flex justify-between">
                    <p className="text-zinc-500">개별 지출 내역</p>
                    <div
                      className="flex items-center"
                      onClick={() => {
                        navigate(`/settlement/expenditure/group/list/${id}/NOT_COMPLETED`);
                      }}>
                      <p className="text-[#0471E9]">내역보기</p>
                      <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                    </div>
                  </div>
                </div>
                <div className="flex items-center space-x-3">
                  <RiHandCoinFill className="text-3xl text-[#f3ba4f]" />
                  <div className="w-full flex justify-between">
                    <p className="text-zinc-500">모임통장 잔액 정산</p>
                    <div onClick={() => navigate(`/settlement/balance/amount/${id}`)} className="flex items-center">
                      <p className="text-[#0471E9]">정산하기</p>
                      <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                    </div>
                  </div>
                </div>
              </>
            ) : (
              <div className="space-y-4">
                <div className="flex items-center space-x-3">
                  <IoIosListBox className="text-3xl text-[#adb3be]" />
                  <div className="w-full flex justify-between">
                    <p className="text-zinc-500">개별 지출 내역</p>
                    <div
                      className="flex items-center"
                      onClick={() => {
                        navigate(`/settlement/expenditure/group/list/${id}/NOT_COMPLETED`);
                      }}>
                      <p className="text-[#0471E9]">내역보기</p>
                      <MdKeyboardArrowRight className="text-2xl text-zinc-600" />
                    </div>
                  </div>
                </div>
                <p className="text-sm text-zinc-600 text-left">모임장만 정산을 시작할 수 있어요</p>
              </div>
            )}
          </div>
        </div>

        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div
          onClick={() => {
            navigate(`/travelbox/delete/${account.accountNo}/${accountInfo.groupId}`);
          }}
          className="p-6">
          {account.moneyBoxDtos.length > 1 && <button className="text-zinc-400">외화저금통 해지하기</button>}
        </div>
      </div>
    )
  );
};

export default MeetingAccountManagement;
