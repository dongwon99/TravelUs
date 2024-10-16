import React from "react";
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../../api/user";
import LastNameInputMui from "../../../components/card/LastNameInputMui";
import FirstNameInputMui from "../../../components/card/FirstNameInputMui";
import Loading from "../../../components/loading/Loading";

const EnglishNameOfCreateCard = () => {
  const navigate = useNavigate();
  const { groupId } = useParams();
  const [isLoading, setIsLoading] = useState(false);
  const [isvalid, setIsValid] = useState(false);
  const [lastname, setLastName] = useState("");
  const [firstname, setFirstName] = useState("");

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000); // 1초 후에 로딩 상태 해제

    return () => clearTimeout(timer);
  }, []);

  useEffect(() => {
    // 유효성 검사
    if (lastname && firstname) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [lastname, firstname]);

  const handleNext = () => {
    navigate(`/card/${groupId}/create/password/meeting`);
  };

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(`/meetingaccount/${groupId}`);
            }}
            className="text-2xl"
          />
        </div>

        <div className="grid gap-6">
          <div className="grid gap-3">
            <div className="flex space-x-2"></div>

            <div className="text-2xl font-semibold">
              <p>영문 이름을 입력해주세요</p>
            </div>
          </div>

          <LastNameInputMui name={lastname} setName={setLastName} />
          <FirstNameInputMui name={firstname} setName={setFirstName} />
          <p className="text-sm text-zinc-400">해외에서 쓰려면 여권 이름과 같아야 해요.</p>
        </div>
      </div>

      <button
        disabled={!isvalid}
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white ${
          isvalid ? "bg-[#1429A0]" : "bg-zinc-300"
        }`}
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default EnglishNameOfCreateCard;
