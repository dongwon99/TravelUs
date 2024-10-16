// 계좌 내역 타입 선언
export interface AccountHistoryRequest {
  accountNo: string;
  currencyCode?: string;
  startDate?: string;
  endDate?: string;
  transactionType?: string;
  //D:입금, W:출금, TD:이체입금, TW:이체출금, ED:환전입금, EW:환전출금, SD:정산입금, SW:정산출금, CW:카드출금  transactionType: string;
  orderByType: string;
  page?: number;
  size?: number;
}

export interface AccountHistoryResponse {
  transactionUniqueNo: number;
  transactionType: string;
  payeeName: string;
  transactionDate: string;
  transactionAmount: string;
  transactionBalance: string;
  transactionSummary: string;
  currencyCode: string;
}