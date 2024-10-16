import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { exchangeApi } from "../../api/exchange";
import { AllDetailedPredictions, PredictionCurrencyData } from "../../types/exchange";
import { IoCaretUpOutline } from "react-icons/io5";
import { IoCaretDownOutline } from "react-icons/io5";
import { TbEqual } from "react-icons/tb";
import { forecastChartSetup } from "../../utils/forecastChartSetup";
import { formatExchangeRate } from "../../utils/currencyUtils";
import Loading from "../../components/loading/Loading";
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
  TimeScale,
  ChartData,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, TimeScale);

interface Props {
  setExchangeRateFront: (rateFront: number) => void;
  setExchangeRateBack: (rateBack: number) => void;
  currencyCode: keyof AllDetailedPredictions;
}

enum TrendType {
  UPWARD = "UPWARD",
  DOWNWARD = "DOWNWARD",
  STABLE = "STABLE",
}

interface RecommendedRate {
  rate: number;
  percent: number;
}

const ExchangeRateForecast: React.FC<Props> = ({ setExchangeRateFront, setExchangeRateBack, currencyCode }) => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [trendInKorean, setTrendInKorean] = useState<string>("상승할");
  const [selectPercent, setSelectPercent] = useState<number | null>(null);
  const [isChecked, setIsChecked] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const loadPredictions = async () => {
      try {
        const data = await exchangeApi.getPrediction();
        setPredictions(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load predictions");
        setLoading(false);
      }
    };

    loadPredictions();
  }, []);

  useEffect(() => {
    if (predictions) {
      const currencyData = predictions[currencyCode];
      if (isPredictionCurrencyData(currencyData)) {
        setTrendInKorean(getTrendInKorean(currencyData.trend as TrendType));
      }
    }
  }, [predictions, currencyCode]);

  const isPredictionCurrencyData = (data: any): data is PredictionCurrencyData => {
    return data && typeof data === "object" && "trend" in data;
  };

  const getTrendInKorean = (trend: TrendType): string => {
    switch (trend) {
      case TrendType.UPWARD:
        return "상승할";
      case TrendType.DOWNWARD:
        return "하락할";
      case TrendType.STABLE:
        return "안정적일";
      default:
        return "예측 불가능한";
    }
  };

  const getRecommendedRates = (
    currencyData: PredictionCurrencyData
  ): { safeData: RecommendedRate[]; cheapData: RecommendedRate[] } => {
    const { forecast_stats } = currencyData;
    const multiplier = currencyCode === "JPY" ? 100 : 1;

    const safeData: RecommendedRate[] = [
      { rate: forecast_stats.p90 * multiplier, percent: 90 },
      { rate: forecast_stats.p80 * multiplier, percent: 80 },
      { rate: forecast_stats.p70 * multiplier, percent: 70 },
    ];
    const cheapData: RecommendedRate[] = [
      { rate: forecast_stats.p10 * multiplier, percent: 10 },
      { rate: forecast_stats.p20 * multiplier, percent: 20 },
      { rate: forecast_stats.p30 * multiplier, percent: 30 },
    ];
    return { safeData, cheapData };
  };

  const handleSelect = (rate: number, percent: number) => {
    const rateString = rate.toFixed(2);
    const [front, back] = rateString.split(".");
    setExchangeRateFront(Number(front));
    setExchangeRateBack(Number(back));
    setSelectPercent(percent);
  };

  const formatRate = (rate: number): string => {
    if (currencyCode === "JPY") {
      return (rate * 100).toFixed(2);
    }
    return rate.toFixed(2);
  };

  if (loading) return <Loading />;
  if (error) return <div>Error: {error}</div>;
  if (!predictions) return null;

  const currencyData = predictions[currencyCode];
  if (!isPredictionCurrencyData(currencyData)) return null;

  const { safeData, cheapData } = getRecommendedRates(currencyData);
  const flagImagePath = `/assets/flag/flagOf${currencyCode}.png`;

  const chartData: ChartData<"line"> = {
    labels: Object.keys(currencyData.forecast),
    datasets: [
      {
        label: `${currencyCode} 예측`,
        data: Object.values(currencyData.forecast),
        borderColor: currencyData.trend === TrendType.UPWARD ? "rgb(221, 82, 87)" : "rgb(72, 128, 238)",
        tension: 0.1,
      },
    ],
  };

  const chartOptions = forecastChartSetup(currencyCode, formatExchangeRate, currencyData.trend);

  return (
    <div className="grid gap-5">
      <div className="grid gap-3">
        <p className="text-lg font-semibold tracking-wide">튜나뱅크가 추천하는 환율이에요</p>

        {/* 추천 환율 */}
        {[safeData, cheapData].map((data, idx) => (
          <div className="w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col border-2 border-transparent" key={idx}>
            <div className="space-y-3">
              <div className="text-lg font-semibold flex space-x-1">
                <p className="text-[#1429A0]">{idx === 0 ? "안전한" : "더 저렴한"}</p>
                <p className="text-zinc-700">자동환전 추천 환율</p>
              </div>

              <div className="grid grid-cols-3 gap-3">
                {data.map((item: RecommendedRate, index: number) => (
                  <div className="flex flex-col items-center justify-center space-y-1" key={index}>
                    <button
                      onClick={() => handleSelect(item.rate, item.percent)}
                      className={`w-full py-2 text-sm font-semibold rounded-3xl border-2 transition-all duration-300 ease-in-out ${
                        selectPercent === item.percent
                          ? "bg-[#1429A0] text-white border-[#1429A0]"
                          : "bg-white text-zinc-600 border-[#b7c4dd]"
                      }`}>
                      {item.rate.toFixed(2)}원{currencyCode === "JPY" && <span className="text-xs ml-1"></span>}
                    </button>
                    <p className="text-sm text-zinc-500">백분위수 {item.percent}%</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="">
        <p className="text-lg font-semibold tracking-wide">튜나뱅크의 환율 예측 정보예요</p>

        {/* 환율 추세 및 현재 환율 */}
        <div className="w-full p-3 rounded-lg bg-[#f4f4f4] flex flex-col border-2 border-transparent">
          <div className="flex justify-between space-y-1">
            <div className="ml-1 flex items-center">
              <img src={flagImagePath} alt={`${currencyCode} Flag`} className="w-6 h-5 mr-2 rounded-sm" />
              <h1 className="text-xl font-semibold">{currencyCode}</h1>
            </div>
            <div>
              <p className="text-sm text-zinc-500 text-right">실시간 환율</p>
              <div className="text-xl font-semibold text-right">
                {formatRate(currencyData.current_rate)}원
                {currencyCode === "JPY" && <span className="text-sm ml-1"></span>}
              </div>
              <div className="text-sm flex items-center space-x-1">
                <p>2주 간</p>
                <p
                  className={
                    currencyData.trend === TrendType.UPWARD
                      ? "text-red-500"
                      : currencyData.trend === TrendType.DOWNWARD
                      ? "text-blue-500"
                      : "text-gray-500"
                  }>
                  {trendInKorean} 것으로 예측
                </p>
                {currencyData.trend === TrendType.UPWARD && <IoCaretUpOutline className="text-red-500" />}
                {currencyData.trend === TrendType.DOWNWARD && <IoCaretDownOutline className="text-blue-500" />}
                {currencyData.trend === TrendType.STABLE && <TbEqual className="text-gray-500" />}
              </div>
            </div>
          </div>
        </div>

        {/* 그래프 표시 토글 */}
        <div className="form-control">
          <label className="label cursor-pointer flex justify-end space-x-3">
            <span className="label-text text-[#565656]">환율 그래프 표시</span>
            <input
              type="checkbox"
              checked={isChecked}
              onChange={(e) => setIsChecked(e.target.checked)}
              className="toggle"
            />
          </label>
        </div>

        {/* 예측 차트 */}
        {isChecked ? (
          <div className="w-full p-3 rounded-lg flex flex-col border-2 border-transparent">
            <div className="h-64">
              <Line data={chartData} options={chartOptions} />
            </div>
          </div>
        ) : (
          <></>
        )}
      </div>
    </div>
  );
};

export default ExchangeRateForecast;
