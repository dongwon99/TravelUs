
import api from "../lib/axios";
import { AccountInfo, MeetingAccountInfo, AccountParticipants, GeneralAccountCreate, MeetingAccountCreate, MeetingAccountUpdateInfo, AccountPassword, ExchangeMode } from "../types/account";
import { AccountHistoryRequest } from "../types/accountHistory";
import { TargetRate } from "../types/exchange";
import { NewParticipant, TravelboxInfo, TravelboxDeleteInfo } from "../types/meetingAccount";

export const accountApi = {
  // 일반 계좌 정보 가져오기
  fetchAccountInfo: async (userId: number): Promise<AccountInfo[]> => {
    const response = await api.get(`/account/general/${userId}/all`)
    return response.data
  },

  // 외화 계좌 정보 가져오기
  fetchForeignAccountInfo: async (userId: number): Promise<AccountInfo[]> => {
    const response = await api.get(`/account/foreign/${userId}/all`)
    return response.data
  },
  
  // 사용자의 모든 계좌 조회
  fetchAllAccountInfo: (searchType: string) => {
    return api.post(`/accounts/inquireAccountList`, { searchType });
  },

  // 특정 계좌 조회
  fetchSpecificAccountInfo: (accountNo: string) => {
    return api.post(`/accounts/inquireAccount`, { accountNo });
  },

  // 모임통장 참여자 정보 가져오기
  fetchParticipantInfo: async (accountId: number): Promise<AccountParticipants> => {
    const response = await api.get(`/account/${accountId}/participants`)
    return response.data
  },

  // 입출금통장 생성
  fetchCreateGeneralAccount: (data: GeneralAccountCreate) => {
    return api.post(`/accounts/createAccount`, data);
  },

  // 모임통장 생성
  fetchCreateMeetingAccount: async (userId: number, data: MeetingAccountCreate): Promise<MeetingAccountCreate> => {
    const response = await api.post(`/account/general/${userId}`, data);
    return response.data;
  },

  // 생성한 모임통장 정보 가져오기 (모임장인 경우)
  fetchJoinedMeetingAccount: async (): Promise<MeetingAccountInfo[]> => {
    const response = await api.get(`/groups/joined`);
    return response.data;
  },

  // 가입한 모임통장 정보 가져오기 (모임원인 경우)
  fetchCreatedMeetingAccount: async (): Promise<MeetingAccountInfo[]> => {
    const response = await api.get(`/groups/created`);
    return response.data;
  },

  // 특정 모임 조회
  fetchSpecificMeetingAccount: (groupId: number) => {
    return api.get(`/groups/${groupId}`);
  },

  // 특정 외화모임통장 조회
  fetchForeignMeetingAccount: async (accountId: number): Promise<AccountInfo> => {
    const response = await api.get(`/account/foreign/accountId/${accountId}`);
    return response.data;
  },

  // 모임 초대 코드 발급
  fetchInvitationCode: (groupId: number) => {
    return api.post(`/groups/create/groupCode`, { "groupId": groupId });
  },

  // 모임원 추가
  fetchCreateParticipant: (groupId: number, data: NewParticipant) => {
    return api.post(`/groups/participants/${groupId}`, data);
  },

  // 초대코드로 모임 정보 조회
  fetchGroupIdByInvitationCode: (code: string) => {
    return api.get(`/groups/code/${code}`);
  },

  // 모임정보 수정
  fetchUpdateMeetingAccount: (groupId: number, data: MeetingAccountUpdateInfo) => {
    return api.put(`/groups/${groupId}`, data);
  },

  // 거래 내역 조회
  fetchTracsactionHistory: (data: AccountHistoryRequest) => {
    return api.get(`/transaction/history`, { params: data });
  },

  // 외화저금통 개설
  fetchCreateTravelBox: (data: TravelboxInfo) => {
    return api.post(`/accounts/addMoneyBox`, data)
  },

  // 외화저금통 해지
  fetchDeleteTravelBox: (accountPassword: string, accountNo: string, currencyCode: string) => {
    return api.delete(`/accounts/moneyBox`, {
      data: {
        accountPassword: accountPassword,
        accountNo: accountNo,
        currencyCode: currencyCode
      }
    })
  },

  // 계좌 비밀번호 검증
  fetchCheckAccountPassword: (data: AccountPassword) => {
    return api.post(`/accounts/validate-password`, data);
  },

  // 참여자 모임 탈퇴
  deleteParticipantId: (participantId: number) => {
    return api.delete(`/groups/participants/${participantId}`);
  },

  // 환전 모드 변경
  fetchChangeExchangeMode: (data: ExchangeMode) => {
    return api.put(`/exchange/mode`, data);
  },

  // 희망 환율 조회
  fetchAutoExchangeType: (groupId: number) => {
    return api.get(`/exchange/rate/target-rate/${groupId}`);
  },

  // 희망 환율 수정
  fetchChangeAutoExchangeInfo: (data: TargetRate) => {
    return api.put(`/exchange/rate/target-rate`, data);
  },

  // 희망 환율 삭제
  deleteAutoExchangeInfo: (groupId: number) => {
    return api.delete(`/exchange/rate/target-rate/${groupId}`);
  }
};