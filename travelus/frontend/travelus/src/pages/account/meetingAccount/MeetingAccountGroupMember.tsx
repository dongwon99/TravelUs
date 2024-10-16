import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { accountApi } from "../../../api/account";
import { userApi } from "../../../api/user";
import { IoIosArrowBack } from "react-icons/io";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MeetingAccountInfo } from "../../../types/account";
import { ParticipantInfo } from "../../../types/meetingAccount";
import Loading from "../../../components/loading/Loading";

const MeetingAccountGroupMember = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const numId = Number(id);
  const [accountInfo, setAccountInfo] = useState<MeetingAccountInfo | null>(null);
  const [memberList, setMemberList] = useState<ParticipantInfo[] | null>(null);
  const [userId, setUserId] = useState<number | null>(null);
  const [groupCode, setGroupCode] = useState("");
  const [participantId, setParticipantId] = useState<number | null>(null);
  const [isUserMaster, setIsUserMaster] = useState(false);

  const handleExit = async () => {
    if (!participantId) return;

    try {
      const response = await accountApi.deleteParticipantId(participantId);
      if (response.status === 201) {
        alert(`${accountInfo?.groupName} 모임을 나왔어요.`);
        navigate("/meetingaccountlist");
      }
    } catch (error) {
      console.log("accountApi의 deleteParticipantId : ", error);
    }
  };

  const getInvitationCode = async () => {
    try {
      const response = await accountApi.fetchInvitationCode(numId);
      setGroupCode(response.data.groupCode);
    } catch (error) {
      console.log("account의 fetchInvitationCode : ", error);
    }
  };

  const shareKakao = () => {
    getInvitationCode();
  };

  // 해당 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(numId);
      if (response.status === 200) {
        setAccountInfo(response.data);
        setMemberList(response.data.participants);
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  // 모임 참여자가 userId로 본인인지 확인
  const fetchUser = async () => {
    try {
      const response = await userApi.fetchUser();
      if (response.status === 200) {
        const myId = response.data.userId;
        setUserId(myId);
      }
    } catch (error) {
      console.error("모임원인지 확인 에러", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
    fetchUser();
  }, []);

  useEffect(() => {
    if (groupCode !== "" && accountInfo && memberList) {
      window.Kakao.Link.sendCustom({
        templateId: 112239,
        templateArgs: {
          groupLeader: memberList && memberList[0].userName,
          groupName: accountInfo?.groupName,
          code: groupCode,
        },
      });
    }
  }, [groupCode]);

  useEffect(() => {
    if (memberList && userId) {
      const storedUserId = localStorage.getItem("userId");
      setParticipantId(memberList.find((member) => member.userId === Number(storedUserId))?.participantId ?? null);

      const currentUser = memberList.find((member) => member.userId === userId);
      setIsUserMaster(currentUser?.master || false);
    }
  }, [memberList, userId]);

  if (!accountInfo && !memberList) {
    return <Loading />;
  }

  return (
    <div className="h-full flex flex-col justify-between">
      <div>
        <div className="py-5 flex flex-col space-y-9">
          <div className="px-5 items-end">
            <IoIosArrowBack
              onClick={() => {
                navigate(`/meetingaccount/management/${id}`);
              }}
              className="text-2xl"
            />
          </div>

          <p className="px-5 text-2xl font-bold">튜나뱅크 모임통장</p>

          <div className="w-full h-5 bg-[#F6F6F8]"></div>

          <div className="px-5">
            <p className="text-lg font-semibold">모임원</p>
            <p className="text-[#565656]">{memberList?.length}명</p>
          </div>
        </div>

        <div className="px-5 space-y-3">
          {memberList?.map((member, index) => (
            <div key={index} className="flex items-center space-x-5">
              <img className="w-11 h-11 rounded-full borde" src={member.profile} alt="유저아이콘" />
              <div className="w-full flex justify-between">
                <p className="font-semibold">{member.userName}</p>
                <div className="flex">
                  <p className="text-zinc-500">{userId === member.userId && "나"}</p>
                  <p className="text-zinc-500">{userId === member.userId && member.master && "ﾠ·ﾠ"}</p>
                  <p className="text-zinc-500">{member.master ? "모임장" : ""}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="p-5 pb-8 space">
        <button
          onClick={() => shareKakao()}
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">
          초대하기
        </button>
        {!isUserMaster && (
          <button
            className="w-full h-14 text-lg text-[#DD1F36] text-center font-semibold tracking-wide"
            onClick={() => handleExit()}>
            모임 나가기
          </button>
        )}
      </div>
    </div>
  );
};

export default MeetingAccountGroupMember;
