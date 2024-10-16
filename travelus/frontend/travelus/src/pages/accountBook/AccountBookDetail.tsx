import React, { useEffect, useState } from "react";
import DropdownInput from "../../components/accountBook/DropdownInput";
import AccountBookCalendar from "../../components/accountBook/AccountBookCalendar";

import AccountBookDayDetail from "../../components/accountBook/AccountBookDayDetail";
import { accountApi } from "../../api/account";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";
import { MeetingAccountInfo } from "../../types/account";
import { useLocation } from "react-router";

const AccountBookDetail = () => {
  const location = useLocation();
  const [isLoading, setIsLoading] = useState(false);
  const [accountNo, setAccountNo] = useState<string>(location.state && location.state.accountNo);
  const [meetingAccountList, setMeetingAccountList] = useState<MeetingAccountInfo[] | null>(null);

  const fetchGroupList = async () => {
    try {
      setIsLoading(true);
      const createdResponse = await accountApi.fetchCreatedMeetingAccount();
      const joinedResponse = await accountApi.fetchJoinedMeetingAccount();

      setMeetingAccountList([...createdResponse, ...joinedResponse]);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchGroupList();
  }, []);

  if (isLoading || !meetingAccountList) {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

  return (
    <div>
      <div className="p-5 flex flex-col items-center space-y-8">
        <p className="w-full text-xl text-left font-bold">모임통장 머니로그</p>
        <div className="w-full flex flex-col space-y-3">
          <DropdownInput meetingAccountList={meetingAccountList} accountNo={accountNo} setAccountNo={setAccountNo} />

          <AccountBookCalendar accountNo={accountNo} />
        </div>
      </div>
    </div>
  );
};

export default AccountBookDetail;
