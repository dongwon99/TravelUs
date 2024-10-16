import React from "react";

const AccountBookDayDetail = () => {
  // const dayTransaction = {
  //   amount: 24.55,
  //   transactionAt: "2021-11-08T11:44:30.327959",
  //   balance: 5236.36,
  //   store: "Maccheroni Republic",
  // };
  return (
    <div>
      {/* <div className="flex justify-between items-start">
        <div>
          <p className="font-semibold">{transaction.time}</p>
          <p className="text-gray-600">{transaction.description}</p>
        </div>
        <div className="text-right">
          <p className={`font-semibold ${transaction.type === "deposit" ? "text-[#0046FF]" : "text-red-500"}`}>
            {transaction.type === "deposit" ? "+" : "-"}
            {transaction.amount.toLocaleString()} {accountInfo.currency}
          </p>
          {showBalance && (
            <p className="text-sm text-gray-500">
              잔액 {transaction.balance.toLocaleString()} {accountInfo.currency}
            </p>
          )}
        </div>
      </div> */}
    </div>
  );
};

export default AccountBookDayDetail;
