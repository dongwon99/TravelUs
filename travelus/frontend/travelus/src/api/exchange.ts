import api from "../lib/axios";
import { ExchangeRateInfo, ExchangeRequest, ExchangeResponse, ExchangeRateHistoryRequest, ExchangeRateHistoryResponse, TargetRate, AllDetailedPredictions } from "../types/exchange";

// import axios from "axios";

// const API_BASE_URL = "http://70.12.130.121:11209"; // GPU 서버

// 환율 예측 정보 및 환율 받아오기
export const exchangeApi = {
  getPrediction: async (): Promise<AllDetailedPredictions> => {
    try {
      const response = await api.get<AllDetailedPredictions>("/exchange/forecast");
      return response.data;
    } catch (error) {
      console.error("Failed to fetch prediction data:", error);
      throw error;
    }
  }
};

// 기본 환율 정보 불러오기
export const exchangeRateApi = {
  getExchangeRates: async (): Promise<ExchangeRateInfo[]> => {
    const response = await api.get<ExchangeRateInfo[]>('/exchange/rate');
    return response.data
  },

  getExchangeRate: async (currencyCode: string): Promise<ExchangeRateInfo> => {
    const response = await api.get<ExchangeRateInfo>(`/exchange/rate/${currencyCode}`);
    return response.data;
  },

  requestExchange: async (data: ExchangeRequest): Promise<ExchangeResponse[]> => {
    const response = await api.post<ExchangeResponse[]>('/transaction/transfer/moneybox', data)
    return response.data
  },

  getExchangeRateHistory: async (data: ExchangeRateHistoryRequest): Promise<ExchangeRateHistoryResponse> => {
    const response = await api.post<ExchangeRateHistoryResponse>('/exchange/latest', data);
    return response.data
  },

  // 환율 저장
  postExchangeTargetRate: (data: TargetRate) => {
    return api.post(`/exchange/rate/target-rate`, data);
  }
}