import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate, useParams } from "react-router";
import { accountApi } from "../../../api/account";
import { GroupInfo } from "../../../types/meetingAccount";
import { exchangeRateApi } from "../../../api/exchange";
import { ExchangeRateInfo } from "../../../types/exchange";
import Loading from "../../../components/loading/Loading";

interface Member {
  participantId: number;
  name: string;
  amount: number;
}

const SettlementInfo = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();

  const [totalAmount, setTotalAmount] = useState(0);
  const [members, setMembers] = useState<Member[]>([]);
  const [groupInfo, setGroupInfo] = useState<GroupInfo | null>(null);
  const [exchangeRate, setExchangeRate] = useState<ExchangeRateInfo>();
  const [isLoading, setIsLoading] = useState(true);

  const handleSettlement = () => {
    const updatedMembers = members.map((member) => {
      const { name, ...rest } = member;
      return rest;
    });

    navigate("/settlement/password", {
      state: {
        groupId: Number(id),
        accountNo: groupInfo?.groupAccountNo,
        amounts: [location.state.koreanAmount, location.state.foreignAmount] as [number, number],
        participants: updatedMembers,
      },
    });
  };

  const handleMembers = () => {
    navigate(`/settlement/editmembers/balance/${id}`, {
      state: {
        members: members,
        koreanAmount: location.state.koreanAmount,
        foreignAmount: location.state.foreignAmount,
      },
    });
  };

  // 금액을 한국 통화 형식으로 포맷(콤마가 포함된 형태)
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        // 모임 조회 성공 시, 바로 통장 정보 조회
        fetchSpecificAccountInfo(response.data.groupAccountNo);
        setGroupInfo(response.data);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      if (exchangeRate?.exchangeRate) {
        setTotalAmount(
          location.state.koreanAmount + Math.ceil(exchangeRate?.exchangeRate * location.state.foreignAmount)
        );
      }
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 환율 정보 가져오기
  const fetchExchangeRate = async () => {
    try {
      const data = await exchangeRateApi.getExchangeRate("USD");
      setExchangeRate(data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchExchangeRate();
  }, []);

  useEffect(() => {
    fetchSpecificMeetingAccount();
  }, [exchangeRate]);

  useEffect(() => {
    if (location.state.members) {
      const memberList = location.state.members;
      const memberCount = memberList.length;
      if (memberList && memberCount > 0) {
        const amountPerMember = Math.floor(totalAmount / memberCount);
        const remainder = totalAmount % memberCount;

        const updatedMembers = memberList.map(
          (member: { participantId: number; name: string; amount: number }, index: number) => ({
            participantId: member.participantId,
            name: member.name,
            amount: index === 0 ? amountPerMember + remainder : amountPerMember,
          })
        );
        setMembers(updatedMembers);
      }
    } else {
      const memberList = groupInfo?.participants;
      const memberCount = groupInfo?.participants.length;
      if (memberList && memberCount && memberCount > 0) {
        const amountPerMember = Math.floor(totalAmount / memberCount);
        const remainder = totalAmount % memberCount;

        const updatedMembers = memberList.map((member, index) => ({
          participantId: member.participantId,
          name: member.userName,
          amount: index === 0 ? amountPerMember + remainder : amountPerMember,
        }));
        setMembers(updatedMembers);
      }
    }
  }, [totalAmount]);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="h-full py-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="px-5 grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack onClick={() => navigate(`/settlement/balance/amount/${id}`)} className="text-2xl" />
          </div>
          <p className="text-lg text-center">정산하기</p>
        </div>

        <div className="grid gap-8">
          <div className="px-5 text-2xl font-semibold tracking-wide">
            <div className="flex">
              <p>총&nbsp;</p>
              <p className="text-[#1429A0]">{exchangeRate?.exchangeRate && formatCurrency(totalAmount)}원</p>
              <p>을</p>
            </div>

            <div className="flex">
              <p className="text-[#1429A0]">{members.length}명</p>
              <p>이 나눕니다</p>
            </div>
          </div>
        </div>

        <div className="px-5 grid gap-5">
          <p className="text-[#565656] underline underline-offset-4 decoration-1" onClick={() => handleMembers()}>
            친구편집
          </p>
          {members.map((item, index) => (
            <div className="flex justify-between items-center" key={index}>
              <div className="flex items-center space-x-3">
                <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                <p>{item.name}</p>
              </div>

              <p>{formatCurrency(item.amount)}원</p>
            </div>
          ))}
        </div>
      </div>

      <div className="px-5">
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => handleSettlement()}>
          정산하기
        </button>
      </div>
    </div>
  );
};

export default SettlementInfo;
