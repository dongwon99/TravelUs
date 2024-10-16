import React from "react";
import { useNavigate } from "react-router";
import { AccountInfo } from "../../types/account";

interface Props {
  isLeader: boolean;
  account: AccountInfo | null;
  foreignAccount: AccountInfo | null;
}

const AccountDetail = ({ isLeader, account, foreignAccount }: Props) => {
  const navigate = useNavigate();

  const navigateSettlement = () => {
    // navigate(`/settlement`)
    navigate(`/selectsettlementamount`);
  };

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatAccountNumber = (accountNo: string) => {
    // 계좌번호를 각 4자리씩 나누고 '-'로 연결
    return accountNo.replace(/(\d{3})(\d{4})(\d{4})(\d{5})/, "$1-$2-$3-$4");
  };

  return (
    <>
      {account && (
        <div>
          <p className="text-sm mb-3 font-bold">일반모임통장</p>
          <div className="w-full mb-8 py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
            <div className="flex flex-col space-y-4">
              <div className="rounded-md flex justify-between">
                <div>
                  <p className="text-sm font-bold">올인원 일반모임통장</p>
                  <p className="text-sm text-zinc-500">{formatAccountNumber(account.accountNo)}</p>
                </div>
                <div className="flex items-center">
                  <p className="text-[1.3rem] font-semibold">{formatCurrency(account.balance)}</p>
                  <p className="text-[1rem]">원</p>
                </div>
              </div>
              <div className="flex space-x-3">
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    navigate("/transaction");
                  }}
                  className="w-full h-9 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
                  입금
                </button>
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    navigate(`/accounthistory/${account.accountNo}`);
                  }}
                  className="w-full h-9 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
                  내역
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {foreignAccount && (
        <div>
          <div className="flex justify-between">
            <p className="text-sm mb-3 font-bold">외화모임통장</p>
            <p>
              {" "}
              희망 환율: {account?.preferenceExchange} {foreignAccount.currency.currencyCode}{" "}
            </p>
          </div>
          <div className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
            <div className="rounded-md flex justify-between mb-3">
              <div className="flex flex-col">
                <p className="text-sm font-bold">올인원 외화모임통장</p>
                <p className="text-sm text-zinc-500">{formatAccountNumber(foreignAccount.accountNo)}</p>
              </div>
              <div className="flex items-center space-x-[0.1rem]">
                <p className="text-[1.3rem] font-semibold">{formatCurrency(foreignAccount.balance)}</p>
                <p className="text-[1rem]">{foreignAccount.currency.currencyCode}</p>
              </div>
            </div>
            <div className="flex space-x-3">
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  navigate("/exchange");
                }}
                className="w-full h-9 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
                환전
              </button>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/accounthistory/${foreignAccount.accountNo}`);
                }}
                className="w-full h-9 p-2 rounded-md bg-[#0046FF] text-white text-sm font-bold">
                내역
              </button>
            </div>
          </div>

          <div className="fixed bottom-2 left-1 right-1 p-4">
            <button
              className="w-full h-12 rounded-md bg-[#0046FF] text-white text-sm font-bold"
              onClick={navigateSettlement}>
              정산하기
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default AccountDetail;
