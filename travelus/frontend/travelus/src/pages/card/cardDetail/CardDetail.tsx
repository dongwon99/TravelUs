import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate, useLocation } from "react-router";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MeetingAccountInfo, TransactionNew, AccountInfoNew } from "../../../types/account";
import { accountApi } from "../../../api/account";
import { cardApi } from "../../../api/card";
import Loading from "../../../components/loading/Loading";
import { set } from "date-fns";

const CardDetail: React.FC = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const groupId = location.state.groupId;
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<AccountInfoNew | null>(null);
  const [transactionList, setTransactionList] = useState<TransactionNew[]>([]);
  const [domesticTotal, setDomesticTotal] = useState(0); // 국내 금액 누적 합계
  const [foreignTotal, setForeignTotal] = useState(0); // 해외 금액 누적 합계
  const [isLoading, setIsLoading] = useState(true);

  const currencySymbols: { [key: string]: string } = {
    KRW: "원",
    USD: "$",
    EUR: "€",
    CNY: "¥",
    JPY: "¥",
  };

  const fetchSpecificMeetingAccount = () => {
    setIsLoading(true);
    accountApi
      .fetchSpecificMeetingAccount(Number(groupId))
      .then((response) => {
        if (response.status === 200) {
          const meetingData = response.data;
          setMeeting(meetingData);
          fetchTotalAmount(meetingData.cardNumber);

          // 두 개의 비동기 요청을 순차적으로 호출
          return Promise.all([
            fetchTransactionHistory(meetingData.groupAccountNo),
            fetchSpecificAccountInfo(meetingData.groupAccountNo),
          ]);
        } else {
          console.log("모임 조회 실패");
        }
      })
      .catch((error) => {
        console.error("모임 조회 에러", error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, [groupId]);

  const formatCardNumber = (cardNumber: string) => {
    // 카드 번호의 앞 4자리와 마지막 4자리를 추출
    const firstFourDigits = cardNumber.slice(0, 4);
    const middleDigits = cardNumber.slice(5, 7);
    const lastFourDigits = cardNumber.slice(-4);

    // 중간 6자리를 '*'로 변환
    const maskedSection = "** ****";

    // 전체 포맷팅된 카드 번호 생성
    return `${firstFourDigits} ${middleDigits}${maskedSection} ${lastFourDigits}`;
  };

  const fetchTransactionHistory = async (accountNo: string) => {
    try {
      const data = {
        accountNo: accountNo,
        orderByType: "DESC",
        transactionType: "CW",
      };

      const response = await accountApi.fetchTracsactionHistory(data);

      if (response.status === 200) {
        setTransactionList(response.data.content);
      }
    } catch (error) {
      console.error("카드 내역 조회 실패", error);
    }
  };

  const fetchSpecificAccountInfo = async (accountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(accountNo);
      if (response.status === 201) {
        setAccount(response.data);
      }
    } catch (error) {
      console.error("계좌 조회 에러", error);
    }
  };

  const fetchTotalAmount = async (cardNo: string) => {
    setIsLoading(true);
    try {
      const response = await cardApi.fetchTotalAmount(cardNo);
      if (response.status === 200) {
        setDomesticTotal(response.data.krwAmount);
        setForeignTotal(response.data.foreignAmount);
      }
    } catch (error) {
      console.error("총 사용 금액 조회 에러", error);
    } finally {
      setIsLoading(false);
    }
  };

  const formatCurrencyNum = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  if (isLoading) {
    return <Loading />;
  }

  return (
    <>
      {meeting && account && (
        <div className="min-h-screen p-5 pb-8 bg-[#F3F4F6]">
          <div className="flex flex-col space-y-5">
            <div className="grid grid-cols-3 items-center">
              <IoIosArrowBack
                onClick={() => {
                  navigate(`/meetingaccount/${groupId}`);
                }}
                className="text-2xl"
              />
              <p className="text-lg text-center">모임카드</p>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl space-y-4">
              <div>
                <p className="text-sm text-zinc-500">모임카드</p>
                <p className="text-lg font-semibold">발급 완료</p>
              </div>

              <div className="flex items-center space-x-3">
                <img className="w-10 h-10" src="/assets/card.png" alt="card" />
                <div>
                  <p className="text-zinc-500">카드 정보</p>
                  <p className="text-zinc-600 text-lg font-semibold">{formatCardNumber(meeting.cardNumber)}</p>
                </div>
              </div>
            </div>

            <div className="w-full bg-white rounded-3xl">
              <div className="p-5 space-y-5">
                <div className="space-y-2">
                  <p className="text-zinc-500">이번 여행 쓴 금액</p>
                  <div className="space-y-1">
                    <div className="flex justify-between items-center">
                      <p className="text-zinc-600">국내</p>
                      <p className="text-2xl font-semibold">
                        {formatCurrencyNum(domesticTotal)} {currencySymbols[account.moneyBoxDtos[0].currencyCode]}
                      </p>
                    </div>
                    <div className="flex justify-between items-center">
                      <p className="text-zinc-600">해외</p>
                      <p className="text-2xl font-semibold">
                        {formatCurrencyNum(foreignTotal)} {currencySymbols[account.moneyBoxDtos[1].currencyCode]}
                      </p>
                    </div>
                  </div>
                </div>
                <div className="flex justify-between">
                  <p className="text-zinc-600">카드 신청 끝</p>
                  <p className="text-blue-500 text-sm">국내 / 해외 결제 가능</p>
                </div>
              </div>

              <div className="w-full border border-zinc-100"></div>

              <div
                onClick={() => {
                  navigate(`/cardtransaction/${meeting.groupId}`);
                }}
                className="p-4">
                <p className="text-center text-zinc-500">내역 더 보기</p>
              </div>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl">
              <div className="space-y-5">
                <p className="font-semibold">카드 관리</p>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">결제 일시정지</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">카드 비밀번호 변경</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">결제 한도 안내</p>
                  <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                </div>
              </div>
            </div>

            <div className="w-full p-5 bg-white rounded-3xl">
              <div className="space-y-5">
                <p className="font-semibold">연결 정보</p>
                <div className="flex justify-between items-center">
                  <p className="text-zinc-500">연결 계좌</p>
                  <div
                    onClick={() => {
                      navigate(`/meetingaccount/${meeting.groupId}`);
                    }}
                    className="flex items-center space-x-2">
                    <p className="text-zinc-600">{meeting.groupName}</p>
                    <MdKeyboardArrowRight className="text-2xl text-zinc-400" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default CardDetail;
