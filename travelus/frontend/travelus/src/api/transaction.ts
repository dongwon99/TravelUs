import api from "../lib/axios";
import { TransferRequest, TransferResponse, TransferRequestNew, TransferResponseNew } from "../types/transaction";

export const transactionApi = {
  // 이체하기
  TransferInfo: async (accountNo: string, data: TransferRequest): Promise<TransferResponse> => {
    const response = await api.post<TransferResponse>(`/transaction/${accountNo}/transfer`, data)
    return response.data
  },

  // 일반 이체
  Transfer: async (data: TransferRequestNew): Promise<TransferResponseNew> => {
    const response = await api.post<TransferResponseNew>(`/transaction/transfer/general`, data)
    return response.data
  },
};