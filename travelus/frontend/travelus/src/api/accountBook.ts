import api from "../lib/axios";
import { DayHistory, DayHistoryCreateInfo, getAccountBookDayQuery, getAccountBookQuery } from "../types/accountBook";

export const accountBookApi = {
  // 월별 가계부 정보 가져오기
  fetchAccountBookInfo: (accountNo: string, data: getAccountBookQuery) => {
    return api.get(`/account-book/history/${accountNo}`, { params: data });
  },

  // 일자별 거래내역 목록
  fetchAccountBookDayInfo: (accountNo: string, date: string, transactionType: string) => {
    return api.get(`/account-book/history/${accountNo}/detail?date=${date}&transactionType=${transactionType}` );
  },

  // 영수증 업로드 후 OCR 정보 가져오기
  fetchReceiptInfo: (data: FormData) => {
    return api.post(`/account-book/upload/receipt`, data);
  },

  // 가계부 등록
  createAccountBook: (data: DayHistoryCreateInfo) => {
    return api.post(`/account-book/save/history`, data)
  }
}