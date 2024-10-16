import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { useLocation } from "react-router";
import { AccountInfoNew, MeetingAccountInfo } from "../../../../types/account";
import { accountApi } from "../../../../api/account";
import { IoIosArrowBack } from "react-icons/io";
import Loading from "../../../../components/loading/Loading";

interface TransferSetMoneyProps {}

const FillSetMoney: React.FC<TransferSetMoneyProps> = (props) => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const [account, setAccount] = useState<AccountInfoNew | null>(null);
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [generalAccount, setGeneralAccount] = useState<AccountInfoNew | null>(null);
  const [depositAccount, setDepositAccount] = useState<AccountInfoNew | null>(null);
  const [transferAmount, setTransferAmount] = useState<string>("");
  const [isValidation, setIsValidation] = useState<boolean>(false);
  const [exceedsMaxAmount, setExceedsMaxAmount] = useState<boolean>(false);
  const [exceedsUserAmount, setExceedsUserAmount] = useState<boolean>(false);
  const MaxAmount = 100000000;
  const [userAmount, setUserAmount] = useState<number>(0);

  useEffect(() => {
    const numberValue = Number(transferAmount);
    if (numberValue <= MaxAmount && numberValue <= userAmount) {
      setExceedsMaxAmount(false);
      setExceedsUserAmount(false);
      setIsValidation(numberValue > 0 && numberValue <= MaxAmount && numberValue <= userAmount);
    }
  }, [transferAmount]);

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);

        // 모임 조회 성공 시, 바로 통장 정보 조회
        if (meetingData?.groupAccountNo) {
          fetchSpecificAccountInfo(meetingData.groupAccountNo);
        } else {
          console.log("groupAccountNo가 없습니다.");
        }
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  // 특정 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 잔액을 위한 API 호출
        const accountResponse = await accountApi.fetchAllAccountInfo("I");
        setGeneralAccount(accountResponse.data[0]);
        setUserAmount(accountResponse.data[0].moneyBoxDtos[0].balance);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchSpecificMeetingAccount();
    fetchData();
  }, []);

  const formatNumber = (num: string) => {
    return num.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  };

  const handleAccountNoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/[^0-9]/g, ""); // 숫자만 남기기
    const numberValue = Number(value);

    if (numberValue <= MaxAmount && numberValue <= userAmount) {
      setTransferAmount(value);
    } else if (numberValue > MaxAmount) {
      setExceedsMaxAmount(true);
    } else if (numberValue > userAmount) {
      setExceedsUserAmount(true);
    }
  };

  const handleIncrement = (incrementValue: number) => {
    const currentAmount = Number(transferAmount) || 0;
    const newAmount = currentAmount + incrementValue;

    if (newAmount <= MaxAmount && newAmount <= userAmount) {
      setTransferAmount(String(newAmount));
    } else if (newAmount > MaxAmount) {
      setExceedsMaxAmount(true);
    } else if (newAmount > userAmount) {
      setExceedsUserAmount(true);
    }
  };

  const displayValue = transferAmount ? `${formatNumber(transferAmount)}원` : "";

  const convertToWon = (amount: string) => {
    const numberAmount = Number(amount);
    if (numberAmount === 0) return "";

    if (numberAmount === 100000000) return "1억원";

    const 만원 = Math.floor(numberAmount / 10000);
    const 원 = numberAmount % 10000;

    return `${만원 > 0 ? formatNumber(String(만원)) + "만 " : ""}${원 !== 0 ? formatNumber(String(원)) + "원" : "원"}`;
  };

  const convertMoney = convertToWon(transferAmount);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (
      e.key === "Backspace" &&
      transferAmount.length > 0 &&
      e.currentTarget.selectionStart === e.currentTarget.value.length
    ) {
      e.preventDefault();
      setTransferAmount(transferAmount.slice(0, -1));
    }
  };

  if (!account && !meeting) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8">
      <div className="relative h-full flex flex-col justify-between">
        <div className="flex flex-col space-y-10">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meetingaccount/${groupId}`);
            }}
            className="text-2xl"
          />
          <div className="flex flex-col space-y-16">
            <div className="flex flex-col space-y-2">
              <div className="text-lg flex items-center">
                <p className="pr-1 font-semibold">{meeting?.groupName}</p>
                <p>에</p>
              </div>
              <p className="text-sm">튜나뱅크 {meeting?.groupAccountNo}</p>
            </div>

            <div className="flex flex-col space-y-10">
              <div>
                <input
                  onChange={handleAccountNoChange}
                  onKeyDown={handleKeyDown}
                  type="tel"
                  inputMode="numeric"
                  placeholder="얼마를 채울까요?"
                  className={`w-full py-3 text-3xl font-bold outline-none ${
                    transferAmount ? "caret-transparent" : "caret-blue-600"
                  }`}
                  value={displayValue}
                />
                {exceedsMaxAmount ? (
                  <p className="text-red-600">한 번에 1억까지 이체할 수 있어요.</p>
                ) : exceedsUserAmount ? (
                  <p className="text-red-600">잔액을 초과하여 이체할 수 없어요.</p>
                ) : (
                  <p>{convertMoney}</p>
                )}
              </div>
            </div>
          </div>
          <div className="w-full text-[#565656] absolute top-80">
            <div className="px-5 py-4 bg-zinc-100 rounded-lg">
              <p>내 입출금통장 : {formatNumber(String(generalAccount?.moneyBoxDtos[0].balance))} 원</p>
            </div>
            <div className="mt-3 grid grid-cols-4 gap-2">
              <button onClick={() => handleIncrement(10000)} className="py-1 border border-zinc-300 rounded-md">
                +1만
              </button>
              <button onClick={() => handleIncrement(50000)} className="py-1 border border-zinc-300 rounded-md">
                +5만
              </button>
              <button onClick={() => handleIncrement(100000)} className="py-1 border border-zinc-300 rounded-md">
                +10만
              </button>
              <button
                onClick={() => handleIncrement(userAmount - Number(transferAmount))}
                className="py-1 border border-zinc-300 rounded-md">
                전액
              </button>
            </div>
          </div>
        </div>

        <div>
          <button
            onClick={() => {
              navigate(`/meeting/${groupId}/fill/confirm`, {
                state: {
                  transferAmount,
                  generalAccountNo: generalAccount?.accountNo,
                  meetingAccountNo: meeting?.groupAccountNo,
                },
              });
            }}
            className={`w-full h-14 text-lg font-semibold rounded-xl tracking-wide ${
              isValidation ? "text-white bg-[#1429A0]" : "text-[#565656] bg-[#E3E4E4]"
            }`}
            disabled={!isValidation}>
            다음
          </button>
        </div>
      </div>
    </div>
  );
};

export default FillSetMoney;
