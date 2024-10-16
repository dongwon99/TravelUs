import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate } from "react-router";
import ExchangeRateInputMui from "../../components/travelBox/ExchangeRateInputMui";
import ExchangeAmmountInput from "../../components/travelBox/ExchangeAmmountInput";
import { useDispatch, useSelector } from "react-redux";
import { setExchangeTargetRate } from "../../redux/meetingAccountSlice";
import { RootState } from "../../redux/store";
import ForecastWarningDrawer from "../../components/exchange/ExchangeRateForecastWarning";
import ExchangeRateForecast from "../exchange/ExchangeRateForecast";

const AutoCurrencyExchangeRateOfCreateTravelBox = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const [exchangeRate, setExchangeRate] = useState(0);
  const [exchangeRateFront, setExchangeRateFront] = useState(0);
  const [exchangeRateBack, setExchangeRateBack] = useState(0);

  const handleNext = () => {
    dispatch(setExchangeTargetRate({ transactionBalance: 0, targetRate: exchangeRate }));
    navigate("/travelbox/create/auto/exchange/amount", { state: { exchangeType: location.state.exchangeType } });
  };

  useEffect(() => {
    setExchangeRate(Number(exchangeRateFront + "." + exchangeRateBack));
  }, [exchangeRateFront, exchangeRateBack]);

  return (
    <div className="h-full">
      <div>
        <div className="p-5 bg-white flex items-center sticky top-0">
          <IoIosArrowBack onClick={() => navigate(-1)} className="text-2xl" />
        </div>

        <div className="p-5 pb-32 grid gap-10 overflow-y-auto">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">02</p>
              <p className="font-medium">희망 환율 설정</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>희망 환율이 되면</p>
              <p>자동으로 환전할게요</p>
            </div>
          </div>

          <ExchangeRateInputMui
            currencyCode={location.state.currencyCode}
            exchangeRateFront={exchangeRateFront}
            setExchangeRateFront={setExchangeRateFront}
            exchangeRateBack={exchangeRateBack}
            setExchangeRateBack={setExchangeRateBack}
          />
          <ExchangeRateForecast setExchangeRateFront={setExchangeRateFront} setExchangeRateBack={setExchangeRateBack} currencyCode={location.state.currencyCode} />
        </div>
      </div>

      <div className="w-full p-5 pb-8 bg-white fixed bottom-0 z-50">
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => handleNext()}>
          다음
        </button>
      </div>
    </div>
  );
};

export default AutoCurrencyExchangeRateOfCreateTravelBox;
