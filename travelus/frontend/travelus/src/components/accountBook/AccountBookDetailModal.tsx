import React, { useEffect, useState } from "react";
import { format, parseISO } from "date-fns";
import { accountBookApi } from "../../api/accountBook";
import { DayHistoryDetail } from "../../types/accountBook";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";
import BuyItemTable from "./BuyItemTable";

interface Props {
  accountNo: string;
  isModalOpen: boolean;
  setIsModalOpen: (isModalOpen: boolean) => void;
  selectedDate: string;
  detailSelectedIndex: number;
}

const AccountBookDetailModal = ({
  accountNo,
  isModalOpen,
  setIsModalOpen,
  selectedDate,
  detailSelectedIndex,
}: Props) => {
  const [dayHistoryDetail, setDayHistoryDetail] = useState<DayHistoryDetail | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchDateDetailInfo = async () => {
    if (accountNo === "" || selectedDate === "") return;

    try {
      setIsLoading(true);
      const response = await accountBookApi.fetchAccountBookDayInfo(accountNo, selectedDate, "A");
      setDayHistoryDetail(response.data[detailSelectedIndex]);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleBackdropClick = (e: React.MouseEvent) => {
    if ((e.target as Element).classList.contains("modal")) {
      handleModalToggle();
    }
  };

  useEffect(() => {
    fetchDateDetailInfo();
  }, [detailSelectedIndex]);

  // 수입과 지출 합계 계산
  // const totalIncome = dayHistoryDetail
  //   .filter((item) => item.transactionType === "G" || item.transactionType === "1")
  //   .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  // const totalExpenditure = dayHistoryDetail
  //   .filter((item) => item.transactionType === "P")
  //   .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  if (accountNo === "" || selectedDate === "") return <></>;

  if (isLoading || !dayHistoryDetail) {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

  return (
    <>
      <input type="checkbox" id="detail-modal" className="modal-toggle" checked={isModalOpen} readOnly />
      <div className="modal" role="dialog" onClick={handleBackdropClick}>
        <div className="modal-box grid gap-2" onClick={(e) => e.stopPropagation()}>
          <h3 className="font-bold text-lg">
            {dayHistoryDetail.transactionAt
              ? format(dayHistoryDetail.transactionAt, "yyyy-MM-dd HH:mm")
              : "선택된 날짜가 없습니다."}
          </h3>
          {dayHistoryDetail.items && dayHistoryDetail.items.length > 0 ? (
            <>
              <BuyItemTable buyItems={dayHistoryDetail.items} />
            </>
          ) : (
            <>
              <p className="text-sm text-zinc-700">총 {dayHistoryDetail.items.length}건</p>
              <hr className="border border-zinc-400" />
              <p className="mt-5 text-center text-zinc-500">상품이 없습니다.</p>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default AccountBookDetailModal;
