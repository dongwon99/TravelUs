import React from "react";
import { IoIosArrowBack } from "react-icons/io";
import { accountApi } from "../../../../api/account";
import { useLocation, useNavigate } from "react-router";

const CreateRequestOfCreateAccount = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleCreate = async () => {
    const data = {
      accountType: "I",
      accountPassword: location.state.password,
      bankId: 1,
    };

    try {
      // 입출금통장 생성 통신
      const response = await accountApi.fetchCreateGeneralAccount(data);
      if (response.status === 201) {
        navigate(`/account/create/completed`);
      }
    } catch (error) {
      alert("입출금통장 개설에 실패했어요");
      navigate("/");
      console.log(error);
    }
  };

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => { navigate("/account/create/userinfo"); }}
            className="text-2xl" />
        </div>

        <div className="flex flex-col items-center space-y-3">
          <img className="w-2/3" src="/assets/IDSuccess.png" alt="" />

          <div className="text-center grid gap-3">
            <p className="text-2xl font-semibold">본인인증을 완료했어요</p>
            <p className="text-sm text-[#6F7581]">마지막으로 입출금통장 개설을 완료해주세요</p>
          </div>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleCreate()}>
        입출금통장 개설하기
      </button>
    </div>
  );
};
export default CreateRequestOfCreateAccount;
