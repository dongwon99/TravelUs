import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import SecurityNumberKeyboard from "../../../components/common/SecurityNumberKeyboard";
import { settlementApi } from "../../../api/settle";
import Loading from "../../../components/loading/Loading";

const PasswordOfBalanceSettlement = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isLoading, setIsLoading] = useState(false);

  const [password, setPassword] = useState("");

  const handleSettlement = async () => {
    const loc = location.state;
    const settlementType = loc.amounts[0] !== 0 && loc.amounts[1] !== 0 ? "BOTH" : loc.amounts[0] !== 0 ? "G" : "F";

    const data = {
      groupId: loc.groupId,
      accountNo: loc.accountNo,
      accountPassword: password,
      settlementType: settlementType,
      amounts: loc.amounts,
      participants: loc.participants,
    };

    try {
      setIsLoading(true);
      const response = await settlementApi.fetchSettlement(data);
      console.log(response.data);
      navigate("/settlement/balance/completed");
    } catch (error) {
      console.log("", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (password.length === 4) {
      // 통신 함수
      handleSettlement();
    }
  }, [password]);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="h-full grid grid-rows-[2fr_1fr]">
      <div className="flex flex-col justify-center items-center space-y-10">
        <p className="text-xl text-center font-medium leading-tight">
          모임통장의 비밀번호를
          <br />
          입력해주세요
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

export default PasswordOfBalanceSettlement;
