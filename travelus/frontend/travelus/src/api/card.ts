import api from "../lib/axios";
import { CardCreateInfo } from "../types/card";

export const cardApi = {
  // 모임카드 개설
  createCard: (data: CardCreateInfo) => {
    return api.post(`/card/issue`, data);
  },

  // 모임카드 누적 사용 금액 조회
  fetchTotalAmount: (cardNo: string) => {
    return api.get(`/card/history/${cardNo}`);
  },
};