import React, { useEffect, useState } from "react";
import { ExpenditureSettlementDetailInfo } from "../../../../types/settlement";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";
import { useLocation, useNavigate, useParams } from "react-router";
import Loading from "../../../../components/loading/Loading";
import { settlementApi } from "../../../../api/settle";
import { AxiosErrorResponseData } from "../../../../types/axiosError";
import { AxiosError } from "axios";

const ExpenditureSettlementGroupList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id, status } = useParams();
  const [isTab, setIsTab] = useState(status); // 진행 중 : NOT_COMPLETED, 완료 : COMPLETED
  const [isEmpty, setIsEmpty] = useState(false);
  const [dateList, setDateList] = useState<string[]>([]);
  const [settlementList, setSettlementList] = useState<{ [date: string]: ExpenditureSettlementDetailInfo[] }>({});

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  // 날짜 형식 변환 함수
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR", {
      month: "long",
      day: "numeric",
    });
  };

  const handleNext = (settlementId: number) => {
    navigate(`/settlement/expenditure/group/detail/${settlementId}/${isTab}`, { state: { groupId: id } });
  };

  const fetchSettlementList = async () => {
    if (id === undefined) return;

    try {
      const response = await settlementApi.fetchSettlementPersonalGroupList(id);

      // 거래내역을 날짜별로 그룹화하여 병합
      const temp = response.data.reduce(
        (acc: { [date: string]: ExpenditureSettlementDetailInfo[] }, cur: ExpenditureSettlementDetailInfo) => {
          const dateKey = formatDate(cur.settlementRequestTime);
          if (cur.isSettled !== isTab) return acc;

          if (acc[dateKey]) {
            acc[dateKey].push(cur);
          } else {
            acc[dateKey] = [cur];
          }
          return acc;
        },
        {} as { [date: string]: ExpenditureSettlementDetailInfo[] }
      );

      // 각 날짜에 대한 거래 내역을 내림차순 정렬
      Object.keys(temp).forEach((date) => {
        temp[date].sort(
          (a: ExpenditureSettlementDetailInfo, b: ExpenditureSettlementDetailInfo) =>
            new Date(b.settlementRequestTime).getTime() - new Date(a.settlementRequestTime).getTime()
        );
      });

      setSettlementList(temp);
      setIsEmpty(false);
    } catch (error) {
      const axiosError = error as AxiosError;
      if (axiosError.response && axiosError.response.data) {
        const responseData = axiosError.response.data as AxiosErrorResponseData;
        if (responseData.message === "BILLING_HISTORY_DETAIL_NOT_FOUND") {
          setIsEmpty(true);
        }
      }
      console.log("settlementApi의 fetchSettlementPersonalList : ", error);
    }
  };

  useEffect(() => {
    setDateList([]);
    setSettlementList({});
    fetchSettlementList();
  }, [isTab]);

  useEffect(() => {
    setDateList(Object.keys(settlementList).sort().reverse()); // 내림차순 정렬
  }, [settlementList]);

  return (
    <div className="h-full p-5">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" onClick={() => navigate(`/meetingaccount/management/${id}`)} />
        </div>

        <div className="grid gap-10">
          <p className="text-2xl font-semibold">
            여행 동안의 개인 지출을
            <br />
            정산해주세요
          </p>

          <div className="text-center text-lg font-semibold grid grid-cols-2">
            <p
              className={`pb-1 ${isTab === "NOT_COMPLETED" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("NOT_COMPLETED")}>
              진행 중
            </p>
            <p
              className={`pb-1 ${isTab === "COMPLETED" ? "border-b-[3px] border-[#565656]" : "text-[#9e9e9e]"}`}
              onClick={() => setIsTab("COMPLETED")}>
              완료
            </p>
          </div>

          {isEmpty || dateList.length === 0 ? (
            <p className="text-center">정산 내역이 없습니다.</p>
          ) : dateList.length === 0 ? (
            <Loading />
          ) : (
            dateList.map((date, index) => (
              <div className="grid gap-5" key={index}>
                <p className="text-[#565656]">{date}</p>
                <div className="grid gap-10">
                  {settlementList[date].map((settlement, index) => (
                    <div
                      className={`grid ${
                        isTab === "NOT_COMPLETED" ? "grid-rows[2fr_1fr]" : ""
                      } grid-cols-[1fr_5fr_1fr] gap-y-2`}
                      key={index}
                      onClick={() => handleNext(settlement.personalSettlementId)}>
                      <div className="flex items-center">
                        <img className="w-10 h-10" src="/assets/user/userIconSample.png" alt="" />
                      </div>

                      <div>
                        <p className="text-xl font-bold tracking-tight">{formatCurrency(settlement.totalAmount)}원</p>
                        <p className="text-[#565656]">
                          {settlement.participants[0].participantName} 외 {settlement.participants.length - 1}명
                        </p>
                      </div>

                      <div className="flex justify-end items-center">
                        <IoIosArrowForward className="text-lg" />
                      </div>

                      {isTab === "NOT_COMPLETED" ? (
                        <p className="col-start-2 text-sm text-[#0046FF] tracking-wide">
                          {formatCurrency(settlement.remainingAmount)}원 미완료
                        </p>
                      ) : (
                        <></>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ExpenditureSettlementGroupList;
