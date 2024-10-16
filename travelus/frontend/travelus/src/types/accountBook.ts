export interface getAccountBookQuery {
  startDate: string;
  endDate: string;
  transactionType: string;
  orderByType: string;
}

export interface getAccountBookDayQuery {
  date: string;
  transactionType: string;
}

export interface DayHistory {
  totalExpenditureForeign : number;
  totalExpenditureKRW: number;
  totalIncomeForeign : number;
  totalIncomeKRW: number;
}

export interface DayHistoryDetail {
  store: string;
  payeeName: string;
  address: string;
  transactionSummary: string;
  paid: number;
  currency: string;
  transactionAt: string;
  items: BuyItemInfo[];
}

export interface DayHistoryCreateInfo {
  accountNo: string;
  store: string;
  paid: number;
  transactionAt: string;
  address: string;
  items: BuyItemInfo[];
  currency: string;
}

export interface ReceiptInfo {
  store: string;
  paid: number;
  transactionAt: string;
  address: string;
  currency: string;
  items: BuyItemInfo[];
}

export interface BuyItemInfo {
  item: string;
  price: number;
  quantity: number;
}