import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartData,
  ChartOptions,
  TimeScale,
} from "chart.js";
import { IoIosArrowBack } from "react-icons/io";
import { exchangeApi } from "../../api/exchange";
import { AllDetailedPredictions, currencyNames } from "../../types/exchange";
import { setupChart } from "../../utils/chartSetup";
import { forecastChartSetup } from "../../utils/forecastChartSetup";
import { calculateDailyChange, formatExchangeRate } from "../../utils/currencyUtils";
import Loading from "../loading/Loading";
import { setCurrentFooterMenu } from "../../redux/accountSlice";
import { useDispatch } from "react-redux";


ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale);

// Types
type PeriodKey = "1주" | "1달" | "3달" | "2주";
type TabType = "exchange" | "prediction";

type CurrencyData = Exclude<AllDetailedPredictions[keyof AllDetailedPredictions], string>;

// Type guard
function isCurrencyData(data: any): data is CurrencyData {
  return data && typeof data === "object" && "recent_rates" in data;
}

// Custom hook for fetching data
const useExchangeData = (currencyCode: string) => {
  const [exchangeData, setExchangeData] = useState<CurrencyData | null>(null);
  const [historicalData, setHistoricalData] = useState<{ date: string; rate: number }[]>([]);
  const [predictionData, setPredictionData] = useState<{ date: string; rate: number }[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await exchangeApi.getPrediction();
        const currencyData = response[currencyCode as keyof AllDetailedPredictions];

        if (isCurrencyData(currencyData)) {
          const rates = Object.entries(currencyData.recent_rates["3_months"]).sort(
            ([dateA], [dateB]) => new Date(dateB).getTime() - new Date(dateA).getTime()
          );

          setExchangeData(currencyData);
          setHistoricalData(rates.map(([date, rate]) => ({ date, rate })));

          if ("forecast" in currencyData && Object.keys(currencyData.forecast).length > 0) {
            setPredictionData(Object.entries(currencyData.forecast).map(([date, rate]) => ({ date, rate })));
          }
        } else {
          throw new Error("Unexpected data structure for currency");
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : "An unknown error occurred");
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [currencyCode]);

  return { exchangeData, historicalData, predictionData, isLoading, error };
};

// Component
const ExchangeDetail: React.FC = () => {
  const { currencyCode = "" } = useParams<{ currencyCode: string }>();
  const flagImagePath = `/assets/flag/flagOf${currencyCode}.png`;
  const navigate = useNavigate();
  const dispatch = useDispatch();
  // const chartRef = useRef<ChartJS<"line">>(null);

  const { exchangeData, historicalData, predictionData, isLoading, error } = useExchangeData(currencyCode);
  const [selectedPeriod, setSelectedPeriod] = useState<PeriodKey>("1주");
  const [activeTab, setActiveTab] = useState<TabType>("exchange");

  const currencyName = currencyNames[currencyCode] || "알 수 없는 통화";

  const getFilteredData = (data: typeof historicalData) => {
    const now = new Date();
    let startDate: Date;
    switch (selectedPeriod) {
      case "1주":
        startDate = new Date(now.setDate(now.getDate() - 7));
        break;
      case "1달":
        startDate = new Date(now.setMonth(now.getMonth() - 1));
        break;
      case "3달":
        startDate = new Date(now.setMonth(now.getMonth() - 3));
        break;
      default:
        startDate = new Date(now.setDate(now.getDate() - 7));
    }
    return data.filter((item) => new Date(item.date) >= startDate);
  };

  const filteredHistoricalData = getFilteredData(historicalData);
  const filteredPredictionData = getFilteredData(predictionData);

  // 어제의 환율과 일일 변화량 계산
  const yesterdayRate = filteredHistoricalData[1]?.rate;
  const dailyChange =
    exchangeData && yesterdayRate ? calculateDailyChange(exchangeData.current_rate, yesterdayRate) : 0;

  // const handlePeriodChange = (newPeriod: PeriodKey) => setSelectedPeriod(newPeriod);

  const chartData: ChartData<"line"> = {
    labels: (activeTab === "exchange" ? filteredHistoricalData : filteredPredictionData).map((data) => data.date),
    datasets: [
      {
        label: activeTab === "exchange" ? "실제 환율" : "예측 환율",
        data: (activeTab === "exchange" ? filteredHistoricalData : filteredPredictionData).map((data) => data.rate),
        borderColor: dailyChange >= 0 ? "rgb(221, 82, 87)" : "rgb(72, 128, 238)",
        tension: 0.1,
      },
    ],
  };

  // 차트 옵션 설정
  const isIncreasing = dailyChange >= 0;
  const chartOptions: ChartOptions<"line"> = setupChart(currencyCode, formatExchangeRate, isIncreasing);

  // Render functions
  const renderExchangeTab = () => (
    <>
      <div className="bg-gray-100 rounded-md p-4 mb-6">
        <h2 className="mb-1">실시간 환율</h2>
        <div className="flex justify-between">
          <span className="font-semibold">
            {exchangeData && formatExchangeRate(exchangeData.current_rate, currencyCode)} 원
          </span>
          <span className={`${isIncreasing ? "text-[#DD5257]" : "text-[#4880EE]"}`}>
            전일대비 {formatExchangeRate(Math.abs(dailyChange), currencyCode)} 원{isIncreasing ? "▲" : "▼"}
          </span>
        </div>
      </div>
      {renderChart()}
      {renderPeriodButtons()}
    </>
  );

  const renderPredictionTab = () => {
    const trend = exchangeData?.trend || "STABLE";
    const chartOptions = forecastChartSetup(currencyCode, formatExchangeRate, trend);

    return (
      <>
        <div className="bg-gray-100 rounded-md p-4 mb-6">
          <h2 className="mb-1">환율 예측</h2>
          <div className="flex justify-between">
            <span className="font-semibold">
              {formatExchangeRate(
                filteredPredictionData.reduce((sum, data) => sum + data.rate, 0) / filteredPredictionData.length,
                currencyCode
              )}{" "}
              원
            </span>
            {/* <span
                className={`${
                  filteredPredictionData[0]?.rate < filteredPredictionData[filteredPredictionData.length - 1]?.rate
                    ? "text-red-500"
                    : "text-blue-500"
                }`}>
                예측 변화{" "}
                {formatExchangeRate(
                  Math.abs(
                    filteredPredictionData[filteredPredictionData.length - 1]?.rate - filteredPredictionData[0]?.rate
                  ),
                  currencyCode
                )}
                {filteredPredictionData[0]?.rate < filteredPredictionData[filteredPredictionData.length - 1]?.rate
                  ? "▲"
                  : "▼"}
              </span> */}
          </div>
        </div>
        <div className="mb-6 h-64">
          <Line
            data={{
              labels: filteredPredictionData.map((data) => data.date),
              datasets: [
                {
                  label: "예측 환율",
                  data: filteredPredictionData.map((data) => data.rate),
                  borderColor:
                    trend === "UPWARD"
                      ? "rgb(221, 82, 87)"
                      : trend === "DOWNWARD"
                      ? "rgb(72, 128, 238)"
                      : "rgb(128, 128, 128)",
                  tension: 0.1,
                },
              ],
            }}
            options={chartOptions}
          />
        </div>
        {renderPeriodButtons()}
      </>
    );
  };

  const renderChart = () => (
    <div className="mb-6 h-64">
      <Line data={chartData} options={chartOptions} />
    </div>
  );

  // const forecastChartSetup = () => (
  //   <div className="mb-6 h-64">
  //     <Line data={chartData} options={chartOptions} />
  //   </div>
  // );

  const renderPeriodButtons = () => {
    if (activeTab === "exchange") {
      return (
        <div className="flex justify-center items-center bg-gray-200 rounded-full p-1">
          {(["1주", "1달", "3달"] as const).map((period) => (
            <button
              key={period}
              onClick={() => setSelectedPeriod(period)}
              className={`px-3 py-1 rounded-full text-center w-full transition-colors duration-300 ${
                selectedPeriod === period ? "bg-white text-[#353535] font-bold shadow-sm" : "text-gray-600"
              }`}>
              {period}
            </button>
          ))}
        </div>
      );
    } else {
      return (
        <div className="flex justify-center items-center bg-gray-200 rounded-full p-1">
          <button
            onClick={() => setSelectedPeriod("2주")}
            className="px-3 py-1 rounded-full text-center w-full bg-white text-[#353535] font-bold shadow-sm">
            금일로부터 2주 예상
          </button>
        </div>
      );
    }
  };

  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <div className="flex justify-center items-center h-screen text-red-500">{error}</div>;
  }

  return (
    <div className="container mx-auto max-w-md h-full p-5 pb-8 flex flex-col">
      <button
        onClick={() => {
          dispatch(setCurrentFooterMenu("환율"));
          navigate("/exchangerate");
        }}
        className="flex items-center text-blue-600 mb-6">
        <IoIosArrowBack className="w-5 h-5 mr-1" />
      </button>
      <div className="flex items-center mb-4">
        <img src={flagImagePath} alt={`${currencyCode} Flag`} className="w-8 h-6 mr-2" />
        <h1 className="text-2xl font-bold">{currencyName}</h1>
      </div>
      <hr className="mb-4" />
      <div className="mb-3 flex justify-center items-center bg-gray-200 rounded-full p-1">
        {(["exchange", "prediction"] as const).map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`flex-1 py-2 text-center ${
              activeTab === tab ? "bg-white text-[#353535] font-bold shadow-sm rounded-full" : "text-gray-600"
            }`}>
            {tab === "exchange" ? "환율" : "환율 예측"}
          </button>
        ))}
      </div>
      {activeTab === "exchange" ? renderExchangeTab() : renderPredictionTab()}

      {/* 채우기 버튼은 아래에 고정 */}
      <div className="flex justify-between mt-auto bottom-0 w-full left-0 bg-white">
        <button
          onClick={() => navigate("/exchange/korean-currency")}
          className="w-[10.5rem] h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
          원화 채우기
        </button>
        <button
          onClick={() => navigate("/exchange/foreign-currency")}
          className="w-[10.5rem] h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
          외화 채우기
        </button>
      </div>
    </div>
  );
};

export default ExchangeDetail;
