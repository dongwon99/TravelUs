import React, { useEffect, useState, useRef } from "react";
import { useLocation, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import { accountApi } from "../../../api/account";
import { cardApi } from "../../../api/card";
import { AccountInfoNew, TransactionNew, MeetingAccountInfo } from "../../../types/account";
import { AccountHistoryResponse } from "../../../types/accountHistory";
import Loading from "../../../components/loading/Loading";

const CardTransaction = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const groupId = useParams().groupId;
  const [account, setAccount] = useState<AccountInfoNew>();
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [transactions, setTransactions] = useState<{ [date: string]: AccountHistoryResponse[] }>({}); // 거래내역 배열
  const [dateList, setDateList] = useState<string[]>([]); // 날짜 리스트
  const [domesticTotal, setDomesticTotal] = useState(0); // 국내 금액 누적 합계
  const [foreignTotal, setForeignTotal] = useState(0); // 해외 금액 누적 합계

  const currencySymbols: { [key: string]: string } = {
    KRW: "원",
    USD: "$",
    EUR: "€",
    CNY: "¥",
    JPY: "¥",
  };

  // 무한 스크롤 관련 상태변수
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const pageEnd = useRef<HTMLDivElement | null>(null); // Intersection Observer가 관찰할 요소

  // 날짜 형식 변환 함수
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR", {
      month: "long",
      day: "numeric",
    });
  };

  // 무한스크롤 데이터 요청을 위해 page를 증가시키는 함수
  const loadMore = () => {
    setPage((prev) => prev + 1);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          const meetingData = response.data;
          setMeeting(meetingData);
          console.log("모임 조회 성공", meetingData);
          fetchSpecificAccountInfo(meetingData.groupAccountNo);
          fetchTotalAmount(meetingData.cardNumber);
        }
      } catch (error) {
        console.error("모임 조회 에러", error);
      }
    };

    fetchData();
  }, [groupId]);

  const fetchTransactionHistory = async () => {
    try {
      if (!meeting) return;

      const data = {
        accountNo: meeting.groupAccountNo,
        orderByType: "DESC",
        transactionType: "CW",
        page: page,
        size: 10,
      };

      const response = await accountApi.fetchTracsactionHistory(data);
      const newTransactions = response.data.content;

      // 거래내역을 날짜별로 그룹화하여 병합
      const newGroupedTransactions = newTransactions.reduce(
        (acc: { [date: string]: AccountHistoryResponse[] }, cur: AccountHistoryResponse) => {
          const dateKey = formatDate(cur.transactionDate);
          if (acc[dateKey]) {
            acc[dateKey].push(cur);
          } else {
            acc[dateKey] = [cur];
          }
          return acc;
        },
        {} as { [date: string]: AccountHistoryResponse[] }
      );

      // 기존 그룹과 새로 불러온 그룹을 병합
      setTransactions((prev) => {
        // 날짜가 중복되면 합치고, 새 날짜면 새롭게 추가
        Object.keys(newGroupedTransactions).forEach((date) => {
          if (prev[date]) {
            prev[date] = [...prev[date], ...newGroupedTransactions[date]];
          } else {
            prev[date] = newGroupedTransactions[date];
          }
        });
        return { ...prev };
      });

      // 새로운 날짜들을 기존 dateList에 추가
      setDateList((prev) => {
        const newDates = Object.keys(newGroupedTransactions);
        const uniqueDates = newDates.filter((date) => !prev.includes(date));
        return [...prev, ...uniqueDates];
      });

      setIsLoading(true);
    } catch (error) {
      console.log(error);
    }
  };

  const fetchSpecificAccountInfo = async (accountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(accountNo);
      if (response.status === 201) {
        setAccount(response.data);
        console.log("account : ", response.data);
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

  useEffect(() => {
    fetchTransactionHistory();
  }, [meeting, page]);

  useEffect(() => {
    if (isLoading && pageEnd.current) {
      // 로딩되었을 때만 실행
      const observer = new IntersectionObserver(
        (entries) => {
          if (entries[0].isIntersecting) {
            loadMore();
          }
        },
        { threshold: 1 }
      );
      // 옵저버 탐색 시작
      observer.observe(pageEnd.current);
    }
  }, [isLoading]);

  const formatCurrency = (amount: string) => {
    const numAmount = Number(amount);
    return new Intl.NumberFormat("ko-KR").format(numAmount);
  };

  const formatCurrencyNum = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const calculateTotals = (transactions: TransactionNew[]) => {
    let domesticSum = 0;
    let foreignSum = 0;

    transactions.forEach((transaction) => {
      if (transaction.currencyCode === "KRW") {
        domesticSum += Number(transaction.transactionAmount);
      } else {
        foreignSum += Number(transaction.transactionAmount);
      }
    });

    setDomesticTotal(domesticSum);
    setForeignTotal(foreignSum);
  };

  if (!account) {
    return <Loading />;
  }

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 bg-white grid grid-cols-3 items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/card/${groupId}`, { state: { groupId } });
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">모임카드</p>
        </div>

        {meeting && account && (
          <div className="p-5">
            <div className="mt-8 mb-5 space-y-7">
              <div className="space-y-5">
                <p className="text-zinc-700">누적 이용 금액</p>
                <div className="space-y-2">
                  <div>
                    <p className="text-sm text-zinc-600">국내</p>
                    <p className="text-2xl font-bold">
                      {formatCurrencyNum(domesticTotal)} {currencySymbols[account?.moneyBoxDtos[0].currencyCode]}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-zinc-600">해외</p>
                    <p className="text-2xl font-bold">
                      {formatCurrencyNum(foreignTotal)} {currencySymbols[account?.moneyBoxDtos[1].currencyCode]}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
        <div className="w-full h-5 bg-[#F6F6F8]"></div>

        <div className="p-5 overflow-y-auto">
          {dateList.length > 0 ? (
            <>
              {dateList.map((date, index) => (
                <div className="grid gap-5" key={index}>
                  <p className="text-zinc-500">{date}</p>

                  {transactions[date].map((transaction, index) => (
                    <div key={index} className="flex flex-col items-center">
                      <div className="w-full flex justify-between">
                        <p className="text-lg font-semibold">{transaction.payeeName}</p>

                        <p className="text-lg tracking-wider">
                          {formatCurrency(transaction.transactionAmount)}
                          {currencySymbols[transaction.currencyCode]}
                        </p>
                      </div>

                      <div className="w-full flex justify-between">
                        <p className="text-sm text-zinc-500 tracking-wider">
                          {new Date(transaction.transactionDate).toLocaleTimeString("ko-KR", {
                            hour: "2-digit",
                            minute: "2-digit",
                            hour12: false,
                          })}
                        </p>
                      </div>
                    </div>
                  ))}
                  <hr className="mb-5" />
                </div>
              ))}

              {/* 무한스크롤에서 인식할 마지막 타겟 */}
              <div ref={pageEnd} className="bg-transparent"></div>
            </>
          ) : (
            <div>
              <p className="text-center">거래 내역이 없습니다</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CardTransaction;
