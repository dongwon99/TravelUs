import api from "../lib/axios";
import { ExpenditureSettlementRequest, SettlementRequest, SettlementTransferRequest } from "../types/settlement";

export const settlementApi = {
  // 잔액 정산 수행
  fetchSettlement: (data: SettlementRequest) => {
    return api.post(`/settlement`, data);
  },

  // 개별 정산 요청
  fetchSettlementPersonal: (data: ExpenditureSettlementRequest) => {
    return api.post(`/settlement/personal`, data);
  },

  // 개별 정산 요청 내역 개인별 조회
  fetchSettlementPersonalList: (settlementStatus: string) => {
    return api.get(`/settlement/personal/transfer?settlementStatus=${settlementStatus}`)
  },

  // 개별 정산금 이체
  fetchSettlementPersonalTransfer: (data: SettlementTransferRequest) => {
    return api.post(`/settlement/personal/transfer`, data);
  },

  // 개별 정산 요청 건당 조회
  fetchSettlementPersonalDetail: (settlementId: number) => {
    return api.get(`/settlement/personal/transfer/${settlementId}`);
  },

  // 개별 정산 요청 내역 모임별 조회
  fetchSettlementPersonalGroupList: (groupId: string) => {
    return api.get(`/settlement/group?groupId=${groupId}`);
  },
};