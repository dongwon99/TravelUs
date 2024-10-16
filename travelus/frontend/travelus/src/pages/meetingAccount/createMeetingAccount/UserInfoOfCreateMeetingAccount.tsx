import { TextField } from "@mui/material";
import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import NameInputMui from "../../../components/meetingAccount/NameInputMui";
import BirthDateInputMui from "../../../components/meetingAccount/BirthDateInputMui";
import GenderInputMui from "../../../components/meetingAccount/GenderInputMui";
import { useNavigate } from "react-router";
import { userApi } from "../../../api/user";
import Loading from "../../../components/loading/Loading";

const UserInfoOfCreateMeetingAccount = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [name, setName] = useState("");
  const [birthdate, setBirthdate] = useState("");
  const [gender, setGender] = useState("여성");

  const handleNext = () => {
    navigate("/meeting/create/meetinginfo");
  };

  const formatDateToKorean = (dateString: string) => {
    const [year, month, day] = dateString.split("-");
    return `${year}년 ${month}월 ${day}일`;
  };

  const getUserInfo = async () => {
    try {
      setIsLoading(true);
      const response = await userApi.fetchUser();
      console.log(response);
      setName(response.data.name);
      setBirthdate(formatDateToKorean(response.data.birth));
      setGender(response.data.gender === "MALE" ? "남성" : "여성");
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  return (
    <>
      {isLoading ? (
        <Loading />
      ) : (
        <div className="h-full p-5 pb-8 flex flex-col justify-between">
          <div className="grid gap-14">
            <div className="flex items-center">
              <IoIosArrowBack className="text-2xl" onClick={() => navigate("/meeting/create/prepare")} />
            </div>

            <div className="grid gap-10">
              <div className="grid gap-3">
                <div className="flex space-x-2">
                  <p className="text-[#1429A0] font-semibold">01</p>
                  <p className="font-medium">모임통장 개설</p>
                </div>

                <div className="text-2xl font-semibold">
                  <p>모임주 {name}님의 정보를</p>
                  <p>확인해주세요</p>
                </div>
              </div>

              <NameInputMui name={name} setName={setName} />
              <BirthDateInputMui birthdate={birthdate} setBirthdate={setBirthdate} />
              <GenderInputMui gender={gender} setGender={setGender} />
            </div>
          </div>

          <button
            className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
            onClick={() => handleNext()}>
            다음
          </button>
        </div>
      )}
    </>
  );
};

export default UserInfoOfCreateMeetingAccount;
