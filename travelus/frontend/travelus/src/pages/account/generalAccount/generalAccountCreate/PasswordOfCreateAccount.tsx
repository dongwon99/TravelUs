import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../../components/common/SecurityNumberKeyboard";
import { IoMdClose } from "react-icons/io";

const PasswordOfCreateAccount = () => {
  const navigate = useNavigate();
  const [password, setPassword] = useState("");

  useEffect(() => {
    if (password.length === 4) {
      navigate("/account/create/password/check", { state: { originalPassword: password } });
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={()=>{navigate("/account/create/userinfo")}}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          입출금 통장에서 사용할
          <br />
          비밀번호를 입력해주세요
        </p>

        <div className="flex space-x-3">
          {[...Array(4)].map((_, index) => (
            <div
              className={`w-4 aspect-1 ${index < password.length ? "bg-[#565656]" : "bg-[#D9D9D9]"} rounded-full`}
              key={index}></div>
          ))}
        </div>
      </div>

      <SecurityNumberKeyboard password={password} setPassword={setPassword} />
    </div>
  );
};

export default PasswordOfCreateAccount;
