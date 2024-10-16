import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { accountApi } from "../../../api/account";
import { userApi } from "../../../api/user";
import { useDispatch, useSelector } from "react-redux";
import { ParticipantInfo } from "../../../types/meetingAccount";
import { RootState } from "../../../redux/store";
import { setCurrentFooterMenu } from "../../../redux/accountSlice";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/free-mode";
import "swiper/css/pagination";

import { IoHome } from "react-icons/io5";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { FaUserFriends, FaBriefcase, FaHeart } from "react-icons/fa";
import { GoHome } from "react-icons/go";
import { FiPlus } from "react-icons/fi";
import { MeetingAccountInfo, MeetingAccountDetailInfo } from "../../../types/account";
import { setTravelboxInfo } from "../../../redux/meetingAccountSlice";
import Loading from "../../../components/loading/Loading";
import { is } from "date-fns/locale";
import { set } from "date-fns";

const MeetingAccountDetail = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { id } = useParams();
  const [userId, setUserId] = useState<number | null>(null);
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [account, setAccount] = useState<MeetingAccountDetailInfo | null>(null);
  const [memberList, setMemberList] = useState<ParticipantInfo[] | null>(null);
  const [isUserMaster, setIsUserMaster] = useState(false);

  const handlleCreateTravelbox = () => {
    if (!account?.accountNo) return;

    dispatch(
      setTravelboxInfo({
        accountPassword: "",
        accountNo: account?.accountNo,
        currencyCode: "",
      })
    );
    navigate("/travelbox/create/currency");
  };

  // 특정 모임 조회 API 호출
  const fetchSpecificMeetingAccount = async () => {
    try {
      const response = await accountApi.fetchSpecificMeetingAccount(Number(id));
      if (response.status === 200) {
        const meetingData = response.data;

        setMeeting(meetingData);
        setMemberList(meetingData.participants);

        // 모임 조회 성공 시, 바로 통장 정보 조회
        if (meetingData?.groupAccountNo) {
          fetchSpecificAccountInfo(meetingData.groupAccountNo);
        } else {
          console.log("groupAccountNo가 없습니다.");
        }
      }
    } catch (error) {
      console.error("모임 조회 에러", error);
    }
  };

  // 특정 모임 통장 조회 API 호출
  const fetchSpecificAccountInfo = async (groupAccountNo: string) => {
    try {
      const response = await accountApi.fetchSpecificAccountInfo(groupAccountNo);
      setAccount(response.data);
    } catch (error) {
      console.error("모임 통장 조회 에러", error);
    }
  };

  // id가 변경될 때마다 모임 조회 실행
  useEffect(() => {
    if (id) {
      fetchSpecificMeetingAccount();
      fetchUser();
    }
  }, [id]);

  // 모임 참여자가 userId로 본인인지 확인
  const fetchUser = async () => {
    try {
      const response = await userApi.fetchUser();
      if (response.status === 200) {
        const myId = response.data.userId;
        setUserId(myId);
      }
    } catch (error) {
      console.error("유저 정보 조회 에러", error);
    }
  };

  useEffect(() => {
    if (memberList && userId) {
      const currentUser = memberList.find((member) => member.userId === userId);
      setIsUserMaster(currentUser?.master || false);
    }
  }, [memberList, userId]);

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

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

    const containerClasses = `w-9 h-9 ${backgroundClass} rounded-full flex justify-center items-center text-white`;
    const iconClasses = "w-6 h-6"; // 아이콘 자체 크기를 줄이기 위한 클래스

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

  if (!account || !meeting || !userId || !memberList) {
    return <Loading />;
  }

  // 클릭하는 통장으로 환전 및 재환전 페이지 라우팅
  const handleExchange = (currencyCode: string) => {
    const selectedAccount = {
      groupId: meeting?.groupId,
      groupName: meeting?.groupName,
      groupAccountNo: meeting?.groupAccountNo,
      currencyCode: currencyCode,
    };
    navigate("/exchange/foreign-currency", { state: { selectedAccount } });
  };

  const handleReExchange = (currencyCode: string) => {
    const selectedAccount = {
      groupId: meeting?.groupId,
      groupName: meeting?.groupName,
      groupAccountNo: meeting?.groupAccountNo,
      currencyCode: currencyCode,
    };
    navigate("/exchange/korean-currency", { state: { selectedAccount } });
  };

  return (
    account &&
    meeting && (
      <div className="h-full bg-[#EBF1FF] grid grid-rows-[35%_65%]">
        <div className="h-full p-5 pb-8">
          <div className="h-full flex flex-col justify-between">
            <div className="flex justify-between items-center">
              <GoHome
                onClick={() => {
                  dispatch(setCurrentFooterMenu("홈"));
                  navigate("/");
                }}
                className="text-2xl text-zinc-600 cursor-pointer"
              />
              <button
                onClick={() => {
                  navigate(`/meetingaccount/management/${meeting.groupId}`);
                }}
                className="w-14 h-8 bg-zinc-500 opacity-40 rounded-3xl text-white font-semibold flex justify-center items-center">
                관리
              </button>
            </div>

            <div className="flex flex-col items-center space-y-5">
              <p className="text-lg font-semibold text-zinc-500">{meeting.groupName}</p>
              <div className="flex flex-col justify-center items-center space-y-1">
                <p>{getIcon(meeting.icon)}</p>
                <p className="text-sm">{getAccountTypeFromIconName(meeting.icon)}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-t-3xl flex flex-col justify-between">
          <div>
            <div
              onClick={() => {
                navigate(`/meetingtransaction/${meeting.groupAccountNo}/detail`, {
                  state: { groupId: meeting.groupId },
                });
              }}
              className="mt-6 p-5 flex flex-col space-y-5">
              <div className="flex justify-between items-center">
                <div className="flex flex-col space-y-1">
                  <p>모임통장</p>
                  <p className="text-2xl font-bold">{formatCurrency(account.moneyBoxDtos[0].balance)}원</p>
                </div>
                {
                  // 카드가 없는 경우에만 버튼 표시
                  !meeting.cardNumber ? (
                    <div
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/card/${meeting.groupId}/create/englishname`);
                      }}
                      className="w-[4.5rem] h-[2.3rem] bg-[#f0f0f0] rounded-3xl flex justify-center items-center">
                      <FiPlus className="text-lg text-blue-500 mr-[0.1rem]" />
                      <button className="text-sm">카드</button>
                    </div>
                  ) : (
                    // 카드가 있을 경우 카드 페이지로 연결
                    <div
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/card/${meeting.groupId}`, { state: { groupId: meeting.groupId } });
                      }}
                      className="w-[4.0rem] h-[2.3rem] bg-[#f0f0f0] rounded-3xl flex justify-center items-center">
                      <button className=" text-zinc-600 font-semibold">카드</button>
                    </div>
                  )
                }
              </div>
              {account.moneyBoxDtos.length > 1 ? (
                <div className="w-full flex justify-between space-x-3">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/meeting/${meeting.groupId}/fill/setmoney`);
                    }}
                    className="w-full h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                    채우기
                  </button>
                  {isUserMaster && (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleExchange("KRW");
                      }}
                      className="w-full h-11 rounded-lg bg-[#1429A0] text-white font-semibold">
                      환전
                    </button>
                  )}
                </div>
              ) : (
                <div className="flex justify-between">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/meeting/${meeting.groupId}/fill/setmoney`);
                    }}
                    className="w-full h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                    채우기
                  </button>
                </div>
              )}
            </div>

            <div className="w-full h-4 bg-[#F6F6F8]"></div>

            <div
              onClick={() => {
                navigate(`/travelbox/transaction/${meeting.groupAccountNo}/detail`, {
                  state: { groupId: meeting.groupId, currencyCode: account.moneyBoxDtos[1].currencyCode },
                });
              }}
              className="p-5 flex flex-col space-y-5">
              <div className="flex flex-col space-y-5">
                {account.moneyBoxDtos.length > 1 ? (
                  <>
                    <div className="flex flex-col space-y-1">
                      <p>외화저금통</p>
                      <p className="text-2xl font-bold">
                        {formatCurrency(account.moneyBoxDtos[1].balance)}
                        <span>{account.moneyBoxDtos[1].currencyCode}</span>
                      </p>
                    </div>
                    {isUserMaster && (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleReExchange(account.moneyBoxDtos[1].currencyCode);
                        }}
                        className="w-full h-11 rounded-lg bg-[#D8E3FF] text-[#026CE1] font-semibold">
                        재환전
                      </button>
                    )}
                  </>
                ) : (
                  <></>
                )}
              </div>
            </div>
          </div>

          {account.moneyBoxDtos.length === 1 && (
            <div className="p-5 pb-8 flex flex-col items-center justify-center space-y-5">
              <button
                className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]"
                onClick={() => {
                  handlleCreateTravelbox();
                }}>
                외화저금통 개설하기
              </button>
            </div>
          )}
        </div>
      </div>
    )
  );
};

export default MeetingAccountDetail;
