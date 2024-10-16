// 계좌 정보
export interface AccountInfo {
  id: number;
  bankCode: number;
  dailyTransferLimit: string;
  accountNo: string;
  balance: number;
  accountName: string;
  accountType: string;
  groupName: string;
  iconName: string;
  travelStartDate: string;
  travelEndDate: string;
  currency: {
    currencyCode: string;
    currencyName: string;
  }
  createdAt: string;
  updatedAt: string;
  preferenceExchange: string;
}

// 계좌 정보 (신규 ver)
export interface AccountInfoNew {
  accountId: number,
  userName: string,
  accountNo: string,
  accountPassword: string,
  accountType: string,
  bankCode: number,
  moneyBoxDtos: Array<{
    moneyBoxId: number,
    balance: number,
    currencyCode: string
  }>,
  createdAt: string,
  updatedAt: string
}

// 모임 정보
export interface MeetingAccountInfo {
  cardNumber: string;
  groupId: number;
  groupAccountNo: string;
  groupName: string;
  icon: string;
  moneyBoxDtoList: Array<{
    moneyBoxId: number;
    balance: number;
    currencyCode: string
  }>;
  participants: Array<{
    createdAt: string;
    master: boolean;
    participantId: number;
    personalAccountNo: string;
    updatedAt: string;
    userId: number;
  }>
};

// 모임통장 상세 정보
export interface MeetingAccountDetailInfo {
  accountId: number;
  accountNo: string;
  accountPassword: string;
  accountType: string;
  bankCode: number;
  moneyBoxDtos: Array<{
    moneyBoxId: number;
    balance: number;
    currencyCode: string
  }>;
  updatedAt: string;
  userName: string;
};


// 거래 내역 조회
export interface Transaction {
  transactionType: string;
  transactionAfterBalance: string;
  transactionUniqueNo: string;
  transactionAccountNo: string;
  transactionBalance: string;
  transactionSummary: string;
  transactionDate: string;
  transactionTime: string;
  transactionMemo: string;
  transactionTypeName: string;
}

// 입금 요청
export interface DepositRequest {
  transactionBalance: number;
  transactionSummary: string;
}

// 입금 응답
export interface DepositResponse {
  transactionUniqueNo: string;
  transactionDate: string;
}

// 모임통장 목록 조회 정보
export interface MeetingAccountListDetail {
  id: number;
  bankCode: number;
  accountPassword: string;
  accountNo: string;
  balance: number;
  accountName: string;
  accountType: string;
  currency: {
    currencyCode: string;
    currencyName: string;
  };
  createdAt: string;
  updatedAt: string;
}

// 모임 정보 수정
export interface MeetingAccountUpdateInfo {
  groupAccountNo: string;
  travelStartDate: string;
  travelEndDate: string;
  groupName: string;
  icon: string;
  }


// 참여자 조회 정보
export interface UserInfo {
  userId: number;
  username: string;
  email: string;
  phone: string;
  address: string;
  birth: string;
  registerAt: string;
}

export interface Participant {
  participantId: number;
  userInfo: UserInfo;
  createdAt: string;
  updatedAt: string;
  master: boolean;
}

export interface AccountParticipants {
  accountId: number;
  participants: Participant[];
}

export interface ParticipantInfo {
  userId: number;
  accountId: number;
  accountNo: string;
}

// 입출금통장 개설 정보
export interface GeneralAccountCreate {
  accountType: string;
  accountPassword: string;
  bankId: number;
}

// 모임통장 개설 정보
export interface MeetingAccountCreate {
  accountType: string;
  accountPassword: string;
  groupName: string;
  travelStartDate: string;
  travelEndDate: string;
  currencyCode: string;
  iconName: string;
  exchangeRate: number;
  participantInfos: Array<ParticipantInfo>;
}

// 일반모임통장 개설 정보
export interface GeneralMeetingAccountDetail {
  generalMeetingAccountName: string;
  generalMeetingAccountIcon: string;
  generalMeetingAccountUserName: string;
  generalMeetingAccountUserResidentNumber: string;
  generalMeetingAccountPassword: string;
  generalMeetingAccountMemberList: Array<ParticipantInfo>;
}

// 모임통장 모임종류 아이콘 
export const meetingAccountIconList: Array<{text: string, value: string}> = [
  { text: "선택안함", value: "airPlane" },
  { text: "친구", value: "friend" },
  { text: "가족", value: "family" },
  { text: "연인", value: "lover" },
  { text: "직장", value: "job" },
];

// 모임통장 초대 코드
export interface meetingInvitationCode {
  groupCode: string;
}

// 거래 내역 조회 (New ver.)
export interface TransactionNew {
  transactionUniqueNo: string;
  transactionType: string;
  accountNo: string;
  transactionDate: string;
  transactionAmount: string;
  transactionBalance: string;
  transactionSummary: string;
  payeeName: string;
  currencyCode: string;
}

// 계좌 비밀번호 확인
export interface AccountPassword {
  accountNo: string;
  accountPassword: string;
}

// 환전 모드 변경
export interface ExchangeMode {
  groupId: number;
  exchangeType: string;
}

// 자동환전 환율 정보
export interface AutoExchangeInfo {
  targetRateId: number;
  amount: number;
  rate: number;
  status: string;
}