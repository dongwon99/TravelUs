import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { endOfMonth, format, parseISO, startOfMonth } from "date-fns";
import "../../css/calendar.css";
import AccountBookDetailModal from "./AccountBookDetailModal";
import Loading from "../loading/Loading";
import { accountBookApi } from "../../api/accountBook";
import { DayHistory, DayHistoryDetail } from "../../types/accountBook";
import { currencyTypeList } from "../../types/exchange";

interface Props {
  accountNo: string;
}

const AccountBookCalendar = ({ accountNo }: Props) => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [firstDate, setFirstDate] = useState<Date | null>(null);
  const [lastDate, setLastDate] = useState<Date | null>(null);
  const [monthlyTransaction, setMonthlyTransaction] = useState<DayHistory[]>([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [dayHistoryDetail, setDayHistoryDetail] = useState<DayHistoryDetail[] | null>(null);

  const handleActiveStartDateChange = (activeStartDate: Date) => {
    const firstDay = startOfMonth(activeStartDate);
    const lastDay = endOfMonth(activeStartDate);
    setFirstDate(firstDay);
    setLastDate(lastDay);
  };

  const handleDateDetail = (date: Date) => {
    if (accountNo === "") return;

    setIsModalOpen(true);
    setSelectedDate(format(date, "yyyy-MM-dd"));
  };

  const fetchAccountBookInfo = async () => {
    if (!firstDate || !lastDate) return;

    const data = {
      startDate: format(firstDate, "yyyy-MM-dd"),
      endDate: format(lastDate, "yyyy-MM-dd"),
      transactionType: "A",
      orderByType: "ASC",
    };

    try {
      setIsLoading(true);
      const response = await accountBookApi.fetchAccountBookInfo(accountNo, data);
      setMonthlyTransaction(response.data.monthHistoryList);
    } catch (error) {
      console.log("accountBookApi의 fetchAccountBookInfo : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchDateDetailInfo = async () => {
    if (accountNo === "" || selectedDate === "") return;

    try {
      setIsLoading(true);
      const response = await accountBookApi.fetchAccountBookDayInfo(accountNo, selectedDate, "A");
      console.log(response);

      // Sort by 'transactionAt' in descending order
      const sortedData = response.data.sort((a: DayHistoryDetail, b: DayHistoryDetail) => {
        return new Date(b.transactionAt).getTime() - new Date(a.transactionAt).getTime();
      });

      setDayHistoryDetail(sortedData);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  useEffect(() => {
    fetchDateDetailInfo();
  }, [selectedDate]);

  useEffect(() => {
    const today = new Date();
    const firstDay = startOfMonth(today);
    const lastDay = endOfMonth(today);
    setFirstDate(firstDay);
    setLastDate(lastDay);
  }, []);

  useEffect(() => {
    if (accountNo === "") return;

    fetchAccountBookInfo();
  }, [accountNo, firstDate, lastDate]);

  if ((accountNo !== "" && isLoading) || !monthlyTransaction || !dayHistoryDetail) {
    <Loading />;
  }

  return (
    <div className="w-full flex flex-col justify-center items-end space-y-3 relative">
      <Calendar
        className="p-3 rounded-md"
        formatDay={(_, date) => format(date, "d")}
        calendarType="gregory"
        showNeighboringMonth={false}
        next2Label={null}
        prev2Label={null}
        minDetail="year"
        onActiveStartDateChange={({ activeStartDate }) =>
          activeStartDate && handleActiveStartDateChange(activeStartDate)
        }
        onClickDay={handleDateDetail}
        tileContent={({ date }) => {
          if (!monthlyTransaction[date.getDate()]) return;
          const day = date.getDate();

          return (
            <div className="h-3 flex justify-center items-center space-x-1">
              {/* 지출 유무 */}
              {monthlyTransaction[day].totalExpenditureForeign + monthlyTransaction[day].totalExpenditureKRW !== 0 && (
                <div className="w-2 h-2 bg-[#DD5257] rounded-full"></div>
              )}

              {/* 수입 유무 */}
              {monthlyTransaction[day].totalIncomeForeign + monthlyTransaction[day].totalIncomeKRW !== 0 && (
                <div className="w-2 h-2 bg-[#4880EE] rounded-full"></div>
              )}
            </div>
          );
        }}
      />

      {accountNo === "" ? (
        <></>
      ) : (
        <button
          className="px-5 py-2 text-center text-white bg-[#1429A0] rounded-lg tracking-wider"
          onClick={() => navigate(`/accountBook/create/info/${accountNo}`)}>
          내역 추가하기
        </button>
      )}

      {/* 일자별 거래 내역 */}
      {dayHistoryDetail && (
        <div className="w-full py-3 grid gap-3">
          {dayHistoryDetail.length > 0 ? (
            <>
              <p className="text-zinc-700">총 {dayHistoryDetail.length}건</p>

              <hr className="border border-zinc-400" />

              <div className="grid gap-3">
                {dayHistoryDetail.map((item, index) => (
                  <div key={index} className="p-3 bg-white rounded-lg flex flex-col space-y-4">
                    <div className="flex justify-between items-center">
                      <div className="flex flex-col justify-center">
                        {item.store === "" ? (
                          <p className="font-semibold">{item.payeeName}</p>
                        ) : (
                          <p className="font-semibold">{item.store}</p>
                        )}
                        <p className="text-sm tracking-wider">{format(parseISO(item.transactionAt), "HH:mm")}</p>
                      </div>
                      <p className={`font-semibold flex justify-end ${item.store === "" ? "" : "text-[#4880EE]"}`}>
                        {item.store === ""
                          ? `- ${parseFloat(String(item.paid)).toLocaleString()}`
                          : `${parseFloat(String(item.paid)).toLocaleString()}`}
                        {item.currency === "KRW"
                          ? "원"
                          : currencyTypeList.find((temp) => temp.value === item.currency)?.text.slice(-2, -1)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </>
          ) : (
            <>
              <p className="text-sm text-zinc-700">총 {dayHistoryDetail.length}건</p>
              <hr className="border border-zinc-400" />
              <p className="mt-5 text-center text-zinc-500">내역이 없습니다.</p>
            </>
          )}
        </div>
      )}

      {/* <AccountBookDetailModal
        accountNo={accountNo}
        isModalOpen={isModalOpen}
        setIsModalOpen={setIsModalOpen}
        selectedDate={selectedDate}
      /> */}
    </div>
  );
};

export default AccountBookCalendar;
