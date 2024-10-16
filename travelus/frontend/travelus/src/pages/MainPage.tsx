import React, { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router";
import { accountApi } from "../api/account";
import { AccountInfoNew, MeetingAccountInfo } from "../types/account";
import { currencyNames } from "../types/exchange";
import { Swiper, SwiperSlide } from "swiper/react";
import { Pagination } from "swiper/modules";
import { IoIosArrowForward } from "react-icons/io";
import MainMeetingAccount from "../components/mainpage/MainMeetingAccount";
import "../css/swiper.css";
import "swiper/css/pagination";
import "swiper/css";
import { ExchangeRateInfo } from "../types/exchange";
import { exchangeRateApi } from "../api/exchange";
import { IoMdAdd } from "react-icons/io";
import { useDispatch } from "react-redux";
import { setCurrentFooterMenu } from "../redux/accountSlice";
import Loading from "../components/loading/Loading";

const MainPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [account, setAccount] = useState<AccountInfoNew | null>(null);
  const [meetingAccountList, setMeetingAccountList] = useState<MeetingAccountInfo[] | null>(null);

  const [exchangeRates, setExchangeRates] = useState<ExchangeRateInfo[] | null>(null);
  const scrollContainerRef = useRef<HTMLDivElement>(null);

  const CURRENCY_CODES = ["USD", "JPY", "EUR"] as const;
  type CurrencyCode = (typeof CURRENCY_CODES)[number];

  const handleCurrencyClick = (code: CurrencyCode) => {
    dispatch(setCurrentFooterMenu("환율"));
    navigate(`exchangerate/${code}`);
  };

  const navigateTransfermation = () => {
    navigate("/transfer/selectbank");
  };

  const navigateAccountBook = () => {
    dispatch(setCurrentFooterMenu("머니로그"));
    navigate("/accountbookdetail");
  };

  const navigateExchangeRate = () => {
    dispatch(setCurrentFooterMenu("환율"));
    navigate("/exchangerate");
  };

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatAccountNumber = (accountNo: string) => {
    // 계좌번호를 각 4자리씩 나누고 '-'로 연결
    return accountNo.replace(/(\d{3})(\d{4})(\d{4})(\d{5})/, "$1-$2-$3-$4");
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 입출금통장 출력을 위한 API 호출
        const accountResponse = await accountApi.fetchAllAccountInfo("I");
        if (accountResponse.data.length > 0) {
          setAccount(accountResponse.data[0]);
        }

        // 모임통장 출력을 위한 API 호출
        const createdResponse = await accountApi.fetchCreatedMeetingAccount();
        const joinedResponse = await accountApi.fetchJoinedMeetingAccount();

        setMeetingAccountList([...createdResponse, ...joinedResponse]);

        // 환율 정보 가져오기
        const exchangeRatesPromises = CURRENCY_CODES.map((code) => exchangeRateApi.getExchangeRate(code));
        const exchangeRatesResponse = await Promise.all(exchangeRatesPromises);
        setExchangeRates(exchangeRatesResponse);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  const getExchangeRate = (currencyCode: CurrencyCode) => {
    if (!exchangeRates) {
      return;
    }

    const rate = exchangeRates.find((r) => r.currencyCode === currencyCode);
    return rate ? rate.exchangeRate.toFixed(2) : "N/A";
  };

  const getLatestUpdateTime = () => {
    if (!exchangeRates) {
      return "N/A";
    }

    return new Date(exchangeRates[0].created).toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  };

  if ((!account && !meetingAccountList) || !exchangeRates) {
    return <Loading />;
  }

  return (
    <div className="w-full">
      <div className="w-full p-5 flex flex-col items-center space-y-4">
        {/* 입출금통장 없을 때 */}
        {account === null ? (
          <div className="w-full p-5 bg-white rounded-xl shadow-md space-y-5">
            <p className="font-semibold">입출금</p>
            <div
              onClick={() => {
                navigate("/account/create/userinfo");
              }}
              className="flex items-center space-x-4">
              <div className="w-10 h-10 bg-blue-200 rounded-full flex justify-center items-center">
                <img className="w-9 h-9" src="/assets/travelusLogo.png" alt="통장 아이콘" />
              </div>
              <div className="flex flex-col">
                <p className="text-sm text-zinc-500">튜나뱅크 통장</p>
                <p className="font-semibold">통장 만들기</p>
              </div>
            </div>
            <div
              onClick={() => {
                navigate("/account/create/userinfo");
              }}
              className="flex items-center space-x-4">
              <div className="w-10 h-10 bg-zinc-200 rounded-full flex justify-center items-center">
                <IoMdAdd className="text-3xl text-zinc-400 font-bold" />
              </div>
              <p className="font-bold text-[#949494]">입출금통장 추가하기</p>
            </div>
          </div>
        ) : (
          // 입출금통장 있을 때
          <div className="w-full p-6 flex flex-col space-y-5 rounded-xl bg-white shadow-md">
            <div className="flex justify-between items-center">
              <div className="flex flex-col space-y-2">
                <p className="text-sm">모임통장과 외화통장을 한 번에</p>
                <div>
                  <p className="text-lg font-semibold">더 편한 환전</p>
                  <p className="text-lg font-semibold">해외여행 올인원 모임통장</p>
                </div>
              </div>

              <div>
                <img className="w-20" src="/assets/bankBookIcon.png" alt="올인원모임통장" />
              </div>
            </div>
            <button
              className="h-10 rounded-md bg-[#1429A0] font-semibold text-white text-sm"
              onClick={() => {
                navigate("/meeting/create/prepare");
              }}>
              개설하기
            </button>
          </div>
        )}

        {/* 입출금 통장 있을 시 표시 */}
        {account && (
          <div className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
            <div
              className="flex flex-col space-y-3"
              onClick={() => {
                navigate(`/transaction/${account.accountNo}`);
              }}>
              <div className="flex justify-between items-center">
                <div className="flex flex-col">
                  <p className="font-semibold">튜나뱅크머니통장</p>
                  <p className="text-sm text-zinc-500">입출금 {formatAccountNumber(account.accountNo)}</p>
                </div>
              </div>
              <div className="flex items-center">
                <p className="text-[1.3rem] font-semibold">{formatCurrency(account.moneyBoxDtos[0].balance)}</p>
                <p className="text-[1rem]">원</p>
              </div>
              <hr />
            </div>
            <div className="flex justify-end mt-3">
              <button
                className="h-8 w-14 rounded-3xl bg-[#1429A0] font-bold text-white text-sm"
                onClick={navigateTransfermation}>
                이체
              </button>
            </div>
          </div>
        )}

        <div className="w-full flex flex-col items-center space-y-2">
          {/* 모임 통장 있을 시 표시 */}
          {/* 모임 통장 개수가 1개일 때 */}
          {meetingAccountList && meetingAccountList?.length === 1 && (
            <Swiper
              pagination={{
                dynamicBullets: true,
              }}
              modules={[Pagination]}
              className="mainOneSwiper rounded-xl">
              {meetingAccountList.map((account, index) => (
                <SwiperSlide>
                  <MainMeetingAccount index={index} account={account} />
                </SwiperSlide>
              ))}
            </Swiper>
          )}

          {/* 모임 통장 개수가 여러 개일 때 */}
          {meetingAccountList && meetingAccountList?.length > 1 && (
            <Swiper
              pagination={{
                dynamicBullets: true,
              }}
              modules={[Pagination]}
              className="mainSwiper rounded-xl">
              {meetingAccountList.map((account, index) => (
                <SwiperSlide>
                  <MainMeetingAccount index={index} account={account} />
                </SwiperSlide>
              ))}
            </Swiper>
          )}

          {/* 환율 표시 부분 */}
          <div
            className="w-full p-6 flex flex-col space-y-2 rounded-xl bg-white shadow-md"
            onClick={() => navigateExchangeRate()}>
            <div className="flex items-center space-x-1">
              <p className="text-md font-semibold flex justify-start">환율</p>
              <IoIosArrowForward className="text-[#565656]" />
            </div>
            <div className="flex justify-end">
              <p className="text-sm text-zinc-400">매매기준율 {getLatestUpdateTime()} </p>
            </div>
            <div className="flex justify-between items-center">
              {CURRENCY_CODES.map((code, index) => (
                <React.Fragment key={code}>
                  {index > 0 && <div className="w-[0.8px] h-14 bg-gray-300"></div>}
                  <div
                    onClick={(e) => {
                      e.stopPropagation();
                      handleCurrencyClick(code)
                    }}
                    className="w-full p-1 flex flex-col justify-center items-center space-y-2">
                    <div className="flex justify-center items-center space-x-1">
                      <img className="w-6 h-4 rounded-sm" src={`/assets/flag/flagOf${code}.png`} alt={code} />
                      <p className="text-sm">{currencyNames[code]}</p>
                    </div>
                    <p className="text-lg font-semibold">{getExchangeRate(code)}</p>
                  </div>
                </React.Fragment>
              ))}
            </div>
          </div>
        </div>

        {/* 머니로그 */}
        <div
          className="w-full p-6 flex flex-col space-y-3 rounded-xl bg-[#4e7af3] shadow-md"
          onClick={navigateAccountBook}>
          <div className="flex items-center space-x-1">
            <p className="text-md text-white font-semibold flex justify-start">머니로그</p>
            <IoIosArrowForward className="text-white" />
          </div>
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-8">
              <img className="w-12" src="/assets/budgetIcon.png" alt="가계부아이콘" />
              <div className="flex flex-col">
                <p className="text-zinc-200 text-sm">이번 여행</p>
                <p className="text-zinc-200 text-sm">머니로그 확인하기</p>
              </div>
            </div>
            <IoIosArrowForward className="text-white" />
          </div>
        </div>

        {/* 환전, 카드 신청 */}
        <div className="w-full grid grid-cols-2 gap-5">
          <div
            onClick={() => {
              navigate("/exchange/foreign-currency");
            }}
            className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col justify-between items-start space-y-8">
            <img className="w-12" src="/assets/exchangeMoneyIcon.png" alt="환전아이콘" />
            <div>
              <p className="font-semibold">수수료 없는</p>
              <p className="font-semibold">환전하기</p>
            </div>
          </div>

          <div
            className="w-full h-40 p-5 rounded-xl bg-white shadow-md flex flex-col items-start space-y-8"
            onClick={() => {
              navigate("/settlement/expenditure/list/NOT_COMPLETED");
            }}>
            <img className="w-12" src="/assets/settlementIcon.png" alt="정산아이콘" />
            <div>
              <p className="font-semibold">
                빠르게
                <br />
                정산금 보내기
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
