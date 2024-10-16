// 잔액 정산 요청 시, 모임원 정보
export interface SettlementParticipant {
  participantId: number;
  amount: number;
}

// 잔액 정산 요청
export interface SettlementRequest {
  groupId: number;
  accountNo: string;
  accountPassword: string;
  settlementType: string;
  amounts: [ number, number ];
  participants: Array<SettlementParticipant>;
}

// 개별 지출 정산 목록 조회
export interface SettlementPersonalInfo {
  groupId: number;
  groupName: string;
  settlementId: number;
  settlementDetailId: number;
  participantId: number;
  profile: string;
  totalAmount: number;
  amount: number;
  remainingAmount: number;
  isSettled: string;
  settlementRequestTime: string;
  participantCount: number;
}

// 개별 지출 정산 이체
export interface SettlementTransferRequest {
  settlementDetailId:number;
  withdrawalAccountNo: string;
  depositAccountNo: string;
  transactionBalance: number;
  withdrawalTransactionSummary: string;
  accountPassword: string;
  depositTransactionSummary: string;
}

// 개별 지출 정산 요청
export interface ExpenditureSettlementRequest {
  groupId: number;
  totalAmount: number;
  participants: Array<SettlementParticipant>;
}

// 개별 지출 정산 단건 조회
export interface ExpenditureSettlementDetailInfo {
  personalSettlementId: number;
  settlementRequestTime: string;
  totalAmount: number;
  remainingAmount: number;
  isSettled: string;
  participants: Array<ExpenditureSettlementDetailPargicipant>;
}

// 개별 지출 정산 단건 조회 시, 모임원 정보
export interface ExpenditureSettlementDetailPargicipant {
  settlementDetailId: number;
  participantId: number;
  participantName: string;
  profile: string;
  amount: number;
  remainingAmount: number;
  isSettled: string;
  settlementRequestTime: string;
}