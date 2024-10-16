import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { accountApi } from "../../../api/account";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store";
import { Swiper, SwiperSlide } from "swiper/react";
import { FreeMode, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/free-mode";
import "swiper/css/pagination";

import { IoHome } from "react-icons/io5";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { FaUserFriends, FaBriefcase, FaHeart } from "react-icons/fa";

import { RiHome5Line } from "react-icons/ri";
import AccountDetail from "../../../components/account/AccountDetail";
import { MeetingAccountInfo } from "../../../types/account";
import Loading from "../../../components/loading/Loading";

const JoinedMeetingAccountDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [numberId, setNumberId] = useState(Number(id));

  const joinedAccountList = useSelector((state: RootState) => state.account.joinedAccountList);
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);
  const [selectedAccount, setSelectedAccount] = useState<MeetingAccountInfo | null>(null);
  const [selectedForeignAccount, setSelectedForeignAccount] = useState<MeetingAccountInfo | null>(null);
  const [memberList, setMemberList] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const getAccountTypeFromIconName = (iconName: string) => {
    switch (iconName) {
      case "airPlane":
        return "여행";
      case "friend":
        return "친구";
      case "family":
        return "가족";
      case "lover":
        return "연인";
      case "job":
        return "직장";
      default:
        return "여행";
    }
  };

  const getIcon = (iconName: string) => {
    // 아이콘별 배경색을 정의하는 객체
    const iconBackgrounds: Record<string, string> = {
      airPlane: "bg-[#638ee4]",
      friend: "bg-[#F5E198]",
      family: "bg-[#FFB555]",
      lover: "bg-[#EB8CA2]",
      job: "bg-[#95DBC1]",
      default: "bg-[#638ee4]", // 기본 배경색
    };

    // 해당 아이콘의 배경색을 가져오고, 없으면 기본값 사용
    const backgroundClass = iconBackgrounds[iconName] || iconBackgrounds.default;

    const containerClasses = `w-6 h-6 ${backgroundClass} rounded-full flex justify-center items-center text-white`;
    const iconClasses = "w-4 h-4"; // 아이콘 자체 크기를 줄이기 위한 클래스

    let IconComponent;

    switch (iconName) {
      case "airPlane":
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
      case "friend":
        IconComponent = <FaUserFriends className={iconClasses} />;
        break;
      case "family":
        IconComponent = <IoHome className={iconClasses} />;
        break;
      case "lover":
        IconComponent = <FaHeart className={iconClasses} />;
        break;
      case "job":
        IconComponent = <FaBriefcase className={iconClasses} />;
        break;
      default:
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
    }

    return <span className={containerClasses}>{IconComponent}</span>;
  };

  useEffect(() => {
    // 특정 외화모임통장 조회
    const fetchData = async () => {
      try {
        const response = await accountApi.fetchForeignMeetingAccount(numberId);
        console.log(response);
        // setSelectedForeignAccount(response);
      } catch (error) {
        console.error("Error fetching data:", error);
        alert("계좌 조회에 실패했습니다.");
      }
    };

    fetchData();
  }, [numberId]);

  useEffect(() => {
    // accountList에서 numberId와 일치하는 계좌 찾기
    const account = joinedAccountList.find((acc) => acc.groupId === numberId);
    setSelectedAccount(account || null);
  }, [joinedAccountList, numberId]);

  useEffect(() => {
    const getParticipants = async () => {
      try {
        if (selectedAccount === null) return;

        setLoading(true); // 데이터 로딩 시작
        const response = await accountApi.fetchParticipantInfo(selectedAccount.groupId);
        const participantNames = response.participants.map((participant: any) => participant.userInfo.username);
        setMemberList(participantNames);
      } catch (error) {
        console.error("Error fetching data:", error);
        alert("참여자 정보 조회에 실패했습니다.");
      } finally {
        setLoading(false); // 데이터 로딩 완료
      }
    };

    getParticipants();
  }, [selectedAccount]);

  if (selectedAccount === null) {
    return <p>계좌 정보를 불러오는 중입니다...</p>;
  }

  return (
    <>
      {joinedAccountList.length > 0 && selectedForeignAccount && (
        <div className="w-full h-full pb-16 bg-[#EFEFF5]">
          <div className="p-5 flex flex-col bg-[#c3d8eb]">
            <div className="mb-12 flex space-x-2 items-center justify-start">
              <RiHome5Line
                onClick={() => {
                  navigate("/");
                }}
                className="text-2xl text-zinc-600 cursor-pointer"
              />
              <p className="text-sm font-bold flex items-center">{selectedAccount.groupName}</p>
            </div>

            <div className="w-full flex flex-col items-center space-y-5">
              <div className="w-full p-2 flex justify-center space-x-2">
                {loading ? (
                  <Loading />
                ) : (
                  <Swiper
                    slidesPerView={3.7}
                    spaceBetween={20}
                    freeMode={true}
                    modules={[FreeMode, Pagination]}
                    className="userSwiper">
                    {memberList.map((name, index) => (
                      <SwiperSlide key={index}>
                        <div className="flex flex-col justify-center items-center space-y-1">
                          <img className="w-12" src="/assets/user/userIconSample.png" alt={name} />
                          <p className="text-xs text-zinc-800">{name}</p>
                        </div>
                      </SwiperSlide>
                    ))}
                  </Swiper>
                )}
              </div>
            </div>
          </div>
          {/* <div className="w-full p-5 flex flex-col">
            <div className="mb-3 flex items-center space-x-[9px]">
              {getIcon(selectedAccount.iconName)}
              <p className="text-sm text-zinc-800 font-bold">{getAccountTypeFromIconName(selectedAccount.iconName)}</p>
            </div>
            <hr className="mb-3 border-0 border-t-[0.5px] border-zinc-200" />
            <AccountDetail isLeader={false} account={selectedAccount} foreignAccount={selectedForeignAccount} />
          </div> */}
        </div>
      )}
    </>
  );
};

export default JoinedMeetingAccountDetail;
