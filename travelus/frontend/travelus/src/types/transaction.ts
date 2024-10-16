// 이체 필요 타입
export interface TransferRequest {
  depositAccountNo: string;
  depositTransactionSummary: string;
  transactionBalance: number;
  withdrawlTransactionSummary: string;
}

// 이체 필요 타입 (New ver.)
export interface TransferRequestNew {
  transferType: string;
  withdrawalAccountNo: string;
  accountPassword: string;
  depositAccountNo: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
  depositTransactionSummary: string;
}

// 이체 응답 타입
export interface TransferResponse {
  transactionUniqueNo: string;
  accountNo: string;
  transactionDate: string;
  transactionType: string;
  transactionTypeName: string;
  transactionAccountNo: string;
}

// 이체 응답 타입 (New ver.)
export interface TransferResponseNew {
  transactionUniqueNo: string;
  transactionType: string;
  accountNo: string;
  transactionDate: string;
  transactionAmount: string;
  transactionBalance: string;
  transactionSummary: string;
}