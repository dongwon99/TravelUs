import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState, useRef } from "react";
import { accountApi } from "../../../api/account";
import { IoIosArrowBack } from "react-icons/io";
import { AccountInfoNew } from "../../../types/account";
import { AccountHistoryResponse } from "../../../types/accountHistory";
import Loading from "../../../components/loading/Loading";

const AccountTransaction = () => {
  const navigate = useNavigate();
  const accountNo = useParams().accountNo;
  const [account, setAccount] = useState<AccountInfoNew>();
  const [transactions, setTransactions] = useState<{ [date: string]: AccountHistoryResponse[] }>({}); // 거래내역 배열
  const [dateList, setDateList] = useState<string[]>([]); // 날짜 리스트

  const depositTransactionType = ["D", "TD", "ED", "SD", "CW"];
  const withdrawTransactionType = ["W", "TW", "EW", "SW"];

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

  const fetchSpecificAccountInfo = async () => {
    try {
      if (!accountNo) return;

      const response = await accountApi.fetchSpecificAccountInfo(accountNo);
      if (response.status === 201) {
        setAccount(response.data);
        console.log("account : ", response.data);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  // 거래 내역 조회
  const fetchTransactionHistory = async () => {
    try {
      if (accountNo) {
        const data = {
          accountNo: accountNo,
          currencyCode: "KRW",
          orderByType: "DESC",
          page: page,
          size: 10,
        };
        const response = await accountApi.fetchTracsactionHistory(data);
        console.log("내역 : ", response);
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
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchSpecificAccountInfo();
  }, []);

  useEffect(() => {
    fetchTransactionHistory();
  }, [page]);

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

  if (!account) {
    return <Loading />;
  }

  const formatCurrency = (amount: string) => {
    const numAmount = Number(amount);
    return new Intl.NumberFormat("ko-KR").format(numAmount);
  };

  const formatCurrencyNum = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  return (
    <div className="h-full pb-8">
      <div className="flex flex-col">
        <div className="p-5 bg-white grid grid-cols-3 items-center sticky top-0">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
          <p className="text-lg text-center">입출금통장</p>
        </div>

        <div className="p-5">
          <div className="my-10 space-y-3">
            <p className="text-zinc-500 underline">{accountNo}</p>
            {account && <p className="text-3xl font-bold">{formatCurrencyNum(account.moneyBoxDtos[0].balance)} 원</p>}
          </div>
          <div className="flex justify-between">
            <button className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">카드</button>
            <button
              onClick={() => {
                navigate("/transfer/selectbank");
              }}
              className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
              이체
            </button>
          </div>
        </div>

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
                        <p className="text-lg font-semibold">{transaction.transactionSummary}</p>

                        {withdrawTransactionType.includes(transaction.transactionType) ? (
                          <p className="text-lg tracking-wider">- {formatCurrency(transaction.transactionAmount)}원</p>
                        ) : (
                          <p className="text-lg text-[#026CE1] tracking-wider">
                            {formatCurrency(transaction.transactionAmount)}원
                          </p>
                        )}
                      </div>

                      <div className="w-full flex justify-between">
                        <p className="text-sm text-zinc-500 tracking-wider">
                          {new Date(transaction.transactionDate).toLocaleTimeString("ko-KR", {
                            hour: "2-digit",
                            minute: "2-digit",
                            hour12: false,
                          })}
                        </p>

                        {depositTransactionType.includes(transaction.transactionType) ? (
                          <p className="text-sm text-right text-zinc-500 tracking-wider">
                            {formatCurrency(transaction.transactionBalance)}원
                          </p>
                        ) : (
                          <p className="text-sm text-right text-zinc-500 tracking-wider">
                            {formatCurrency(transaction.transactionBalance)}원
                          </p>
                        )}
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

export default AccountTransaction;
