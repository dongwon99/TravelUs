import React from "react";
import { useState, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { FaCheck } from "react-icons/fa6";
import { FiPlus } from "react-icons/fi";
import { IoIosCheckmarkCircle } from "react-icons/io";
import { IoIosCheckmarkCircleOutline } from "react-icons/io";
import { userApi } from "../../../api/user";
import { cardApi } from "../../../api/card";
import { accountApi } from "../../../api/account";
import { MeetingAccountInfo } from "../../../types/account";
import Loading from "../../../components/loading/Loading";

const AddressOfCreateCard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { groupId } = useParams();
  const [isLoading, setIsLoading] = useState(false);
  const [address, setAddress] = useState("");
  const [meeting, setMeeting] = useState<MeetingAccountInfo | null>(null);
  const [ischecked, setIsChecked] = useState(true);
  const password = location.state.password;

  const handleNext = async () => {
    // 모임카드 개설 API 호출
    setIsLoading(true);

    if (!meeting) {
      console.log("모임 정보가 없습니다.");
      return;
    }

    const data = {
      cardUniqueNo: "DC5O2YKQ",
      withdrawalAccountNo: meeting.groupAccountNo,
      password: password,
    };

    try {
      const response = await cardApi.createCard(data);
      if (response.status === 201) {
        navigate(`/card/${groupId}/create/completed`);
      }
    } catch (error) {
      console.error(error);
      alert("카드 개설에 실패했습니다.");
      navigate("/");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    // 유저 정보 가져오는 API 호출
    const fetchUser = async () => {
      setIsLoading(true);
      try {
        const response = await userApi.fetchUser();
        if (response.status === 200) {
          setAddress(response.data.address);
        }
      } catch (error) {
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };

    // 모임 정보 가져오는 API 호출
    const fetchMeeting = async () => {
      try {
        const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
        if (response.status === 200) {
          setMeeting(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchUser();
    fetchMeeting();
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/card/${groupId}/create/password/card`);
            }}
            className="text-2xl"
          />
        </div>

        <div className="text-2xl font-semibold">
          <p>카드를 받을 주소를 확인해주세요</p>
        </div>

        <div className="flex flex-col space-y-8">
          <div className="flex justify-between">
            <div className="flex items-start space-x-5">
              <img className="w-[1.5rem] h-[1.5rem] mt-1" src="/assets/homeIcon.png" alt="home" />
              <div className="flex flex-col">
                <p className="text-lg font-semibold">집</p>
                <p className="text-sm text-zinc-600">{address}</p>
              </div>
            </div>
            <FaCheck className="mt-1 text-xl text-blue-500" />
          </div>

          <div
            onClick={() => {
              navigate("/userupdate/address");
            }}
            className="flex items-center space-x-5">
            <div className="w-7 h-7 rounded-full bg-[#e9e9e9] flex justify-center items-center">
              <FiPlus className="text-xl text-blue-600" />
            </div>
            <p className="text-lg text-zinc-500 font-semibold">주소 수정하기</p>
          </div>
        </div>
      </div>

      <div className="space-y-10">
        <div
          onClick={() => {
            setIsChecked(!ischecked);
          }}
          className="flex justify-between items-center">
          <div>
            <p className="text-zinc-500">내가 없을 때</p>
            <p className="text-lg font-semibold">동료나 가족이 대신 받기</p>
          </div>
          {ischecked ? (
            <IoIosCheckmarkCircle className="text-3xl text-blue-600" />
          ) : (
            <IoIosCheckmarkCircleOutline className="text-3xl text-blue-600" />
          )}
        </div>
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => handleNext()}>
          선택한 주소로 받기
        </button>
      </div>
    </div>
  );
};

export default AddressOfCreateCard;
