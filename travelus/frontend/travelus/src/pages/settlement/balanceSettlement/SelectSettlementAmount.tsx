import React, { useEffect, useState } from "react";
import { IoIosArrowBack, IoIosCheckmarkCircle, IoIosCheckmarkCircleOutline } from "react-icons/io";
import { LuDot } from "react-icons/lu";
import { useNavigate, useParams } from "react-router";
import { accountApi } from "../../../api/account";
import { MeetingAccountDetailInfo } from "../../../types/account";
import { currencyTypeList, ExchangeRateInfo } from "../../../types/exchange";
import { exchangeRateApi } from "../../../api/exchange";

const SelectSettlementAmount = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [isChecked, setIsChecked] = useState<boolean[]>([]);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountDetailInfo | null>(null);
  const [exchangeRate, setExchangeRate] = useState<ExchangeRateInfo>();
  const [koreanAmount, setKoreanAmount] = useState<string>("");
  const [foreignAmount, setForeignAmount] = useState<string>("");
  const [numberKrw, setNumberKrw] = useState<number>(0);
  const [numberForeign, setNumberForeign] = useState<number>(0);

  const guideTextList = [
    "남은 원화는 일반 모임통장의 잔액이에요.",
    "남은 외화는 외화 모임통장의 잔액이에요.",
    "원화와 외화 함께 정산 시, 외화는 현재 환율을 적용하여 원화로 환전한 후 정산돼요.",
  ];

  const handleSettlement = () => {
    // 남은 원화, 남은 외화 모두 정산
    navigate(`/settlement/balance/participants/${id}`, {
      state: { koreanAmount: numberKrw, foreignAmount: numberForeign },
    });
  };

  const handleCheck = (index: number, checked: boolean) => {
    const updatedChecked = [...isChecked];
    updatedChecked[index] = checked;
    setIsChecked(updatedChecked);
  };

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number | string): string => {
    const numericAmount = typeof amount === "string" ? parseFloat(amount.replace(/,/g, "")) : amount;
    return isNaN(numericAmount) ? "" : numericAmount.toLocaleString("ko-KR");
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${month}월 ${day}일 ${hours}:${minutes}시 기준`;
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        // 모임 조회 성공 시, 바로 통장 정보 조회
        fetchSpecificAccountInfo(response.data.groupAccountNo);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccountInfo(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  // 환율 정보 가져오기
  const fetchExchangeRate = async () => {
    try {
      const data = await exchangeRateApi.getExchangeRate("USD");
      setExchangeRate(data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
    fetchExchangeRate();
  }, []);

  const handleAmountChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    setCurrency: React.Dispatch<React.SetStateAction<string>>,
    setNumber: React.Dispatch<React.SetStateAction<number>>,
    maxAmount: number
  ) => {
    const value = e.target.value.replace(/,/g, "");
    const numericValue = parseFloat(value);
    if (!isNaN(numericValue)) {
      const limitedValue = Math.min(numericValue, maxAmount);
      setCurrency(formatCurrency(limitedValue));
      setNumber(limitedValue);
    } else {
      setCurrency("");
      setNumber(0);
    }
  };

  return (
    <>
      {accountInfo && (
        <div className="h-full p-5 pb-8 flex flex-col justify-between">
          <div className="grid gap-14">
            <div className="grid grid-cols-3">
              <div className="flex items-center">
                <IoIosArrowBack
                  onClick={() => {
                    navigate(`/meetingaccount/management/${id}`);
                  }}
                  className="text-2xl"
                />
              </div>
              <p className="text-lg text-center">정산하기</p>
            </div>

            <div className="grid gap-10">
              <p className="text-2xl font-semibold tracking-wide">
                정산할 금액을
                <br />
                입력해주세요
              </p>

              <div className="grid gap-8">
                {accountInfo.moneyBoxDtos.map((moneyBox, index) => (
                  <div className="grid grid-cols-[1fr_9fr] gap-y-3">
                    <div className="flex items-center">
                      <input
                        id={moneyBox.currencyCode}
                        type="checkbox"
                        className="w-6 h-6 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
                        onChange={(e) => handleCheck(index, e.target.checked)}
                      />
                    </div>

                    <label className="text-xl font-semibold" htmlFor={moneyBox.currencyCode} key={index}>
                      {moneyBox.currencyCode === "KRW" ? "모임통장" : "외화저금통"}
                    </label>

                    <div className={`grid gap-3 col-start-2 ${isChecked[index] ? "" : "opacity-65"}`}>
                      <div className="px-3 py-2 border rounded-md flex justify-end">
                        <input
                          className="text-right outline-none placeholder:text-black"
                          type="text"
                          value={moneyBox.currencyCode === "KRW" ? koreanAmount : foreignAmount}
                          onChange={(e) =>
                            handleAmountChange(
                              e,
                              moneyBox.currencyCode === "KRW" ? setKoreanAmount : setForeignAmount,
                              moneyBox.currencyCode === "KRW" ? setNumberKrw : setNumberForeign,
                              moneyBox.balance
                            )
                          }
                          placeholder="0"
                          disabled={!isChecked[index]}
                        />
                        &nbsp;
                        {moneyBox.currencyCode === "KRW" ? (
                          "원"
                        ) : (
                          <>
                            {currencyTypeList.find((item) => item.value === moneyBox.currencyCode)?.text.slice(-2, -1)}
                          </>
                        )}
                      </div>

                      <p className="px-3 text-[#565656] font-semibold text-right">
                        잔액&nbsp;
                        {formatCurrency(moneyBox.balance)}&nbsp;
                        {moneyBox.currencyCode === "KRW" ? (
                          "원"
                        ) : (
                          <>
                            {currencyTypeList.find((item) => item.value === moneyBox.currencyCode)?.text.slice(-2, -1)}
                          </>
                        )}
                      </p>

                      {moneyBox.currencyCode === "KRW" ? (
                        <></>
                      ) : (
                        <div className="px-3 text-[#565656]">
                          <div className="text-sm grid grid-cols-[1fr_2fr]">
                            <p className="">환율 적용 시</p>

                            <div className="flex justify-end">
                              <p className="text-right">
                                {exchangeRate?.exchangeRate &&
                                  formatCurrency(
                                    Math.ceil(
                                      Number(parseFloat(foreignAmount.replace(/,/g, "") || "0")) *
                                        exchangeRate.exchangeRate
                                    )
                                  )}
                              </p>
                              <p className="">&nbsp; 원</p>
                            </div>
                          </div>

                          <div className="text-sm flex justify-between items-center">
                            <p>{formatDate(exchangeRate?.created ? exchangeRate?.created : "")}</p>
                            <p>
                              1 {moneyBox.currencyCode} = {exchangeRate?.exchangeRate} 원
                            </p>
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                ))}

                <hr />

                <p className="px-3 text-lg text-right font-semibold">
                  총액&nbsp;
                  {exchangeRate?.exchangeRate &&
                    formatCurrency(
                      parseFloat(koreanAmount.replace(/,/g, "") || "0") +
                        Math.ceil(parseFloat(foreignAmount.replace(/,/g, "") || "0") * exchangeRate?.exchangeRate)
                    )}
                  &nbsp;원
                </p>
              </div>
            </div>
          </div>

          <div className="grid gap-5">
            {/* <div className="grid gap-3">
              <p className="text-[#333D4B] font-semibold">정산 안내</p>

              {guideTextList.map((text, index) => (
                <div className="flex" key={index}>
                  <LuDot className="text-lg text-[#8B95A1]" />
                  <p className="w-[90%] text-sm text-[#4E5968] break-keep">{text}</p>
                </div>
              ))}
            </div> */}

            <button
              className={`w-full h-14 text-lg rounded-xl tracking-wide ${
                isChecked[0] || isChecked[1] ? "text-white bg-[#1429A0] " : "text-[#565656] bg-[#E3E4E4]"
              }`}
              onClick={() => handleSettlement()}
              disabled={!(isChecked[0] || isChecked[1])}>
              {isChecked[0] || isChecked[1] ? "정산하기" : "정산금을 선택해주세요"}
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default SelectSettlementAmount;
