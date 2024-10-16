import { Detail } from "react-calendar/dist/cjs/shared/types";

// 환율 타입
export interface ExchangeRateInfo {
  currencyCode: string;
  exchangeRate: number;
  exchangeMin: string;
  created: string;
}

// 환율 그래프를 위한 타입
export interface ExchangeRateHistoryRequest {
  currencyCode: string;
  startDate: string;
  endDate: string;
}

export interface ExchangeRateHistoryResponse {
  id: number;
  postAt: string;
  dealBasR: number;
  ttb: number;
  tts: number;
  cashBuying: number;
  cashSelling: number;
  tcBuying: number;
}

// 환전 진행 관련 요청 / 응답
export interface ExchangeRequest {
  transferType: string;
  accountNo: string;
  accountPassword: string;
  sourceCurrencyCode: string;
  targetCurrencyCode: string;
  transactionBalance: number;
}

export interface ExchangeResponse {
  transactionUniqueNo: string;
  transactionType: string;
  accountNo: string;
  transactionDate: string;
  transactionAmount: string;
  ransactionBalance: string;
  transactionSummary: string;
}

// 통화 종류
export const currencyNames: { [key: string]: string } = {
  USD: '미국 달러',
  JPY: '일본 엔',
  EUR: '유럽 유로',
  TWD: '대만 달러',
};

// 통화 종류 목록
export const currencyTypeList: Array<{text: string, value: string}> = [
  { text: "USD(미국/$)", value: "USD" },
  { text: "JPY(일본/¥)", value: "JPY" },
  { text: "EUR(유로/€)", value: "EUR" },
  { text: "TWD(대만/$)", value: "TWD" },
];

// --------------------------------------------------------------------------------

// 환율 예측 types 선언
export interface ExchangeRateInfo2 {
  currencyCode: string;
  exchangeRate: number;
  lastUpdated: string;
}

export interface RecentRates {
  "3_months": { [date: string]: number };
}

export interface ForecastStats {
  average: number;
  min: number;
  max: number;
  p10: number;
  p20: number;
  p30: number;
  p40: number;
  p50: number;
  p60: number;
  p70: number;
  p80: number;
  p90: number;
  p100: number;
}

export interface PredictionCurrencyData {
  forecast: { [date: string]: number };
  trend: string;
  current_rate: number;
  forecast_stats: ForecastStats;
  recent_rates: RecentRates;
}

// export interface DataCollectionCurrencyData {
//   forecast: {};
//   current_rate: number;
//   recent_rates: RecentRates;
// }

export interface AllDetailedPredictions {
  USD: PredictionCurrencyData;
  JPY: PredictionCurrencyData;
  EUR: PredictionCurrencyData;
  TWD: PredictionCurrencyData;
  last_updated: string;
}

// 개별 통화에 대한 타입 (USD, JPY용)
export type DetailedPrediction = PredictionCurrencyData;

export interface TargetRate {
  groupId: number;
  currencyCode: string;
  transactionBalance: number;
  targetRate: number;
  dueDate: string;
  accountPassword: string;
}