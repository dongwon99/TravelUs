import React, { useEffect, useState } from "react";
import { IoIosArrowBack, IoIosCloseCircle } from "react-icons/io";
import { CiSearch } from "react-icons/ci";
import { useLocation, useNavigate, useParams } from "react-router";
import { accountApi } from "../../api/account";
import { GroupInfo } from "../../types/meetingAccount";

interface Member {
  participantId: number;
  name: string;
  amount: number;
}

const EditMembers = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { type, id } = useParams();

  const [groupInfo, setGroupInfo] = useState<GroupInfo | null>(null);
  const [selectedMembers, setSelectedMembers] = useState<Member[]>([]);

  const handleSelected = () => {
    if (type === "balance") {
      navigate(`/settlement/balance/participants/${id}`, {
        state: {
          members: selectedMembers,
          koreanAmount: location.state.koreanAmount,
          foreignAmount: location.state.foreignAmount,
        },
      });
    } else {
      navigate(`/settlement/expenditure/participants/${id}`, {
        state: { members: selectedMembers, totalAmount: location.state.totalAmount },
      });
    }
  };

  const toggleMemberSelection = (participantId: number, name: string) => {
    // 모임원 체크 해제
    if (selectedMembers.some((member) => member.name === name)) {
      setSelectedMembers(selectedMembers.filter((member) => member.name !== name));
    } else {
      // 모임원 체크
      setSelectedMembers([...selectedMembers, { participantId: participantId, name: name, amount: 0 }]);
    }
  };

  const removeFromSelected = (participantId: number) => {
    setSelectedMembers(selectedMembers.filter((member) => member.participantId !== participantId));
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        setGroupInfo(response.data);
      }
    } catch (error) {
      console.error("accountApi의 fetchSpecificMeetingAccount : ", error);
    }
  };

  useEffect(() => {
    fetchSpecificMeetingAccount();
    if (location.state?.members) {
      setSelectedMembers(location.state.members);
    }
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">모임원 선택</p>
        </div>

        <div className="grid gap-5">
          {/* 선택된 멤버 리스트 */}
          <div className="flex space-x-5">
            {selectedMembers.map((item, index) => (
              <div className="relative" key={index} onClick={() => removeFromSelected(item.participantId)}>
                <div className="flex flex-col justify-center space-y-2">
                  <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                  <p>{item.name}</p>
                </div>
                <button>
                  <IoIosCloseCircle className="opacity-50 absolute top-0 right-0" />
                </button>
              </div>
            ))}
          </div>

          <div className="p-3 bg-[#F3F4F6] rounded-md flex items-center space-x-2">
            <CiSearch className="text-lg" />
            <input type="text" className="w-full text-[#565656] bg-[#F3F4F6] outline-none" placeholder="이름 검색" />
          </div>

          {/* 전체 멤버 리스트 */}
          <div className="grid gap-5">
            {groupInfo &&
              groupInfo.participants.map((item, index) => (
                <label className="flex justify-between items-center" key={index}>
                  <div className="flex items-center space-x-3">
                    <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                    <p>{item.userName}</p>
                  </div>
                  <input
                    type="checkbox"
                    className="w-6 h-6 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
                    checked={selectedMembers.some((member) => member.name === item.userName)}
                    onChange={() => toggleMemberSelection(item.participantId, item.userName)}
                  />
                </label>
              ))}
          </div>
        </div>
      </div>

      {/* 선택된 멤버 수 동기화 */}
      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleSelected()}>
        {selectedMembers.length}명 선택
      </button>
    </div>
  );
};

export default EditMembers;
