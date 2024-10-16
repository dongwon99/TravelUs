import api from "../lib/axios";
import { AccountHistoryRequest, AccountHistoryResponse } from "../types/accountHistory";

export const accountHistoryApi = {
  // 일반 계좌 이체 기록
  AccountHistoryInfo: async (accountNo: string, params: AccountHistoryRequest): Promise<AccountHistoryResponse[]> => {
    const response = await api.get<AccountHistoryResponse[]>(`/transaction/${accountNo}/history`, { params });
    return response.data;
  },

  // 외화 계좌 이체 기록
  ForeignAccountHistoryInfo: async (accountNo: string, params: AccountHistoryRequest): Promise<AccountHistoryResponse[]> => {
    const response = await api.get<AccountHistoryResponse[]>(`/transaction/foreign/${accountNo}/history`, { params });
    return response.data;
  }
};