import React, { useEffect, useState } from "react";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { useLocation, useNavigate, useParams } from "react-router";
import { IoMdClose } from "react-icons/io";


const CheckPasswordOfCreateCard = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { groupId } = useParams();
  const [password, setPassword] = useState("");

  useEffect(() => {
    if (password.length === 4) {
      if (location.state.originalPassword === password) {
        navigate(`/card/${groupId}/create/address`, { state: { password: password } });
      } else {
        alert("비밀번호가 일치하지 않습니다.");
        navigate(`/card/${groupId}/create/password/card`);
      }
    }
  }, [password]);

  return (
    <div className="h-full grid grid-rows-[0.2fr_2fr_1fr]">
      <div
        onClick={() => {
          navigate(`/card/${groupId}/create/englishname`);
        }}
        className="p-4">
        <IoMdClose className="text-3xl" />
      </div>
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          비밀번호를
          <br />한 번 더 입력해주세요
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

export default CheckPasswordOfCreateCard;
