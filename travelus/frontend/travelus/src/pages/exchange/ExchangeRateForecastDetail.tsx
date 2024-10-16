import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Line } from "react-chartjs-2";
import { ChartData } from "chart.js";
import { IoIosArrowBack } from "react-icons/io";
import { exchangeApi } from "../../api/exchange";
import { AllDetailedPredictions, PredictionCurrencyData } from "../../types/exchange";
import { forecastChartSetup } from "../../utils/forecastChartSetup";
import { formatExchangeRate } from "../../utils/currencyUtils";
import Loading from "../../components/loading/Loading";

const ExchangeRateForecastDetail: React.FC = () => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCurrency, setSelectedCurrency] = useState<keyof AllDetailedPredictions>("USD");

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

  // const renderForecastStats = (stats: PredictionCurrencyData["forecast_stats"]) => {
  //   return (
  //     <div className="forecast-stats">
  //       <h3 className="text-lg font-semibold mb-2">예측 통계</h3>
  //       <p>평균: {stats.average.toFixed(2)}</p>
  //       <p>최소: {stats.min.toFixed(2)}</p>
  //       <p>최대: {stats.max.toFixed(2)}</p>
  //       <h4 className="text-md font-semibold mt-4 mb-2">백분위수</h4>
  //       <ul className="list-disc pl-5">
  //         {[10, 20, 30, 40, 50, 60, 70, 80, 90, 100].map((percentile) => (
  //           <li key={percentile}>
  //             {percentile}%: {formatExchangeRate(stats[`p${percentile}` as keyof typeof stats] || 0, selectedCurrency)}
  //           </li>
  //         ))}
  //       </ul>
  //     </div>
  //   );
  // };

  const renderCurrencyPrediction = (
    currency: keyof AllDetailedPredictions,
    prediction: PredictionCurrencyData,
    selectedCurrency: keyof AllDetailedPredictions,
    setSelectedCurrency: (currency: keyof AllDetailedPredictions) => void
  ) => {
    const forecastEntries = Object.entries(prediction.forecast);
    const latestForecastRate = forecastEntries[forecastEntries.length - 1][1];
    const earliestForecastRate = forecastEntries[0][1];
    const currentRate = prediction.current_rate;
    const forecastChange = latestForecastRate - earliestForecastRate;
    const isIncreasing = forecastChange >= 0;
    const flagImagePath = `/assets/flag/flagOf${currency}.png`;

    const getTrendInKorean = (trend: string) => {
      switch (trend) {
        case "UPWARD":
          return "상승할";
        case "DOWNWARD":
          return "하락할";
        case "STABLE":
          return "변동이 없을";
        default:
          return "알 수 없을";
      }
    };

    const trendInKorean = getTrendInKorean(prediction.trend);

    // 추천 희망 환율 계산 함수
    const getRecommendedRates = () => {
      const forecastRates = Object.values(prediction.forecast);
      const stats = prediction.forecast_stats;

      // 1. 최소한의 이득을 원하는 사람
      const minProfitRate = forecastRates.find((rate) => rate < currentRate) || currentRate;

      // 2. 최대의 이득을 원하는 사람
      const maxProfitRate = {
        min: stats.min,
        avg: stats.average,
        max: stats.max,
      };

      // 3. 추천 희망 환율 (10% ~ 90% 사이)
      const recommendedRate = {
        p10: stats.p10,
        p90: stats.p90,
      };

      return { minProfitRate, maxProfitRate, recommendedRate };
    };

    const recommendedRates = getRecommendedRates();

    const chartData: ChartData<"line"> = {
      labels: Object.keys(prediction.forecast),
      datasets: [
        {
          label: `${currency} 예측`,
          data: Object.values(prediction.forecast),
          borderColor: prediction.trend === "UPWARD" ? "rgb(221, 82, 87)" : "rgb(72, 128, 238)",
          tension: 0.1,
        },
      ],
    };

    const chartOptions = forecastChartSetup(currency, formatExchangeRate, prediction.trend);

    return (
      <div key={currency} className="currency-prediction">
        <div className="ml-1 flex items-center mb-4">
          <img src={flagImagePath} alt={`${currency} Flag`} className="w-8 h-6 mr-2" />
          <h1 className="text-2xl font-bold">{currency} 예측</h1>
        </div>
        <div className="bg-gray-100 rounded-md p-4 mb-6">
          <h2 className="mb-1">환율 예측</h2>
          <div className="flex justify-between">
            <span className="font-semibold">{formatExchangeRate(latestForecastRate, currency)}</span>
          </div>
        </div>

        <div className="my-8 h-64">
          <Line data={chartData} options={chartOptions} />
        </div>
        <div className="mb-5 flex justify-center items-center bg-gray-200 rounded-full p-1">
          {(["USD", "JPY", "TWD", "EUR"] as const).map((currency) => (
            <button
              key={currency}
              onClick={() => setSelectedCurrency(currency)}
              className={`flex-1 py-2 text-center ${
                selectedCurrency === currency
                  ? "bg-white text-[#353535] font-bold shadow-sm rounded-full"
                  : "text-gray-600"
              }`}>
              {currency}
            </button>
          ))}
        </div>

        {/* 현재 환율 및 trend 표시 */}
        <div className="bg-gray-100 rounded-md p-4 mb-4">
          <div className="flex justify-between">
            <h2 className="mb-1 font-bold text-2xl">현재 환율</h2>
            <span className="font-semibold">{formatExchangeRate(currentRate, currency)}</span>
          </div>
          <div className="font-bold">
            <span
              className={`${
                trendInKorean === "상승할"
                  ? "text-[#DD5257]"
                  : trendInKorean === "하락할"
                  ? "text-[#4880EE]"
                  : "text-gray-500"
              }`}>
              최근 환율 추세는 {trendInKorean} 것으로 예상이 돼요
            </span>
          </div>
        </div>
        {/* <div className="bg-gray-100 rounded-md p-4 mb-6">{renderForecastStats(prediction.forecast_stats)}</div> */}

        {/* 추천 환율 */}
        <div className="bg-gray-100 rounded-md p-4 mb-4">
          <h2 className="mb-2 font-bold text-2xl">추천 희망 환율</h2>
          <p className="mb-2 font-semibold text-gray-500">튜나뱅크에서 2주 간의 환율 예측 정보를 드려요</p>
          <h3 className="mb-2 font-semibold">
            오늘의 환율보다 조금이라도 떨어지면
            <br /> 환전되기 원하는 고객님들에게 추천해요
          </h3>
          <p className="text-right font-bold mb-4">{formatExchangeRate(recommendedRates.minProfitRate, currency)}</p>
          <h3 className="mb-2 font-semibold">
            2주 간의 예측 중에 최소 환율과 최대 환율
            <br /> 그리고 평균 환율을 알려드릴게요
          </h3>
          <div className="text-right font-bold mb-4">
            <p>최소 환율: {formatExchangeRate(recommendedRates.maxProfitRate.min, currency)}</p>
            <p>평균 환율: {formatExchangeRate(recommendedRates.maxProfitRate.avg, currency)}</p>
            <p>최대 환율: {formatExchangeRate(recommendedRates.maxProfitRate.max, currency)}</p>
          </div>
          <h3 className="mb-2 font-semibold">
            위의 환율 예측 결과로
            <br /> 추천하는 희망 환율 범위를 알려드릴게요
          </h3>
          <p className="text-right font-bold mb-4">
            {formatExchangeRate(recommendedRates.recommendedRate.p10, currency)} ~{" "}
            {formatExchangeRate(recommendedRates.recommendedRate.p90, currency)}
          </p>
        </div>
        <div className="bg-gray-100 rounded-md p-4 mb-4">
          <div className="mb-2">
            환율예측은 매일 진행이 되기 때문에
            <br /> 여행기간이 2주 넘게 남으신 고객님들은 <br />
            환율예측을 매일 진행하시는걸 추천드려요
          </div>
          <div className="text-center">추가적인 데이터 생각중....</div>
        </div>
        {/* {renderForecastStats(prediction.forecast_stats)} */}
      </div>
    );
  };

  if (loading) return <Loading />;
  if (error) return <div>Error: {error}</div>;
  if (!predictions) return <div>No predictions available</div>;

  return (
    <div className="h-full p-5 pb-8">
      <button onClick={() => navigate(-1)} className="flex items-center text-blue-600 mb-6">
        <IoIosArrowBack className="w-5 h-5 mr-1" />
      </button>
      <div className="exchange-rate-forecast-detail">
        <h1 className="text-2xl font-bold mb-4">희망 환율 추천</h1>
        <hr className="mb-4" />
        {predictions[selectedCurrency] &&
          renderCurrencyPrediction(
            selectedCurrency,
            predictions[selectedCurrency] as PredictionCurrencyData,
            selectedCurrency,
            setSelectedCurrency
          )}
      </div>
    </div>
  );
};

export default ExchangeRateForecastDetail;
