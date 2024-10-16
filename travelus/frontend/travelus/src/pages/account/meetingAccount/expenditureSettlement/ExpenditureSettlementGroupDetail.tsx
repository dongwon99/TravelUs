import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate, useParams } from "react-router";
import { settlementApi } from "../../../../api/settle";
import { ExpenditureSettlementDetailInfo } from "../../../../types/settlement";
import Loading from "../../../../components/loading/Loading";

const ExpenditureSettlementGroupDetail = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id, status } = useParams();
  const [settlement, setSettlement] = useState<ExpenditureSettlementDetailInfo | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatDate = (date: string) => {
    const newDate = new Date(date);
    return (
      newDate.getFullYear() +
      "년 " +
      (newDate.getMonth() + 1) +
      "월 " +
      newDate.getDate() +
      "일 " +
      newDate.getHours() +
      ":" +
      newDate.getMinutes()
    );
  };

  const fetchSettlementDetail = async () => {
    try {
      setIsLoading(true);
      const response = await settlementApi.fetchSettlementPersonalDetail(Number(id));
      console.log(response);
      setSettlement(response.data);
    } catch (error) {
      console.log("settlementApi의 fetchSettlementPersonalDetail : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchSettlementDetail();
  }, []);

  if (isLoading || !settlement) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-20">
        <div className="flex items-center">
          <IoIosArrowBack
            className="text-2xl"
            onClick={() => navigate(`/settlement/expenditure/group/list/${location.state.groupId}/${status}`)}
          />
        </div>

        <div className="grid gap-8">
          <div>
            <p className="">
              총 {formatCurrency(settlement.totalAmount)}원 {settlement.isSettled === "NOT_COMPLETED" ? "중" : ""}
            </p>
            <p className="text-3xl font-bold">
              {settlement.isSettled === "NOT_COMPLETED"
                ? `${formatCurrency(settlement.totalAmount - settlement.remainingAmount)}원 완료`
                : "정산 완료"}
            </p>
          </div>

          <div className="grid gap-3">
            <hr />
            <p className="text-sm font-light text-[#8F8F8F]">요청일 {formatDate(settlement.settlementRequestTime)}</p>
          </div>

          <div className="grid gap-5">
            {settlement.participants.map((user, index) => (
              <div className="flex justify-between items-center" key={index}>
                <div className="flex items-center space-x-3">
                  <img className="w-9 h-9 border rounded-full" src={user.profile} alt="" />
                  <p>{user.participantName}</p>
                </div>
                <div className={`${user.isSettled === "NOT_COMPLETED" ? "" : "text-[#8F8F8F]"} flex space-x-3`}>
                  <p>{formatCurrency(user.amount)}원</p>

                  <p className={`w-14 text-right `}>{user.isSettled === "NOT_COMPLETED" ? "미완료" : "완료"}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExpenditureSettlementGroupDetail;
