import api from "../lib/axios";
import { MeetingAccountInfo } from "../types/meetingAccount";

export const groupApi = {
  // 모임통장 개설
  createMeetingAccount: (data: MeetingAccountInfo) => {
    return api.post(`/groups/createGroup`, data);
  },
};