import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import { userApi } from "../../api/user";
import PhoneInput from "../../components/user/inputField/PhoneInput";
import VerificationCodeInput from "../../components/user/inputField/VerificationCodeInput";
import { IoIosArrowBack } from "react-icons/io";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editUserInformation } from "../../redux/userInformationSlice";

interface InputState {
  phone: string;
  verificationCode: string;
}

const UserPhoneUpdate = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [step, setStep] = useState(0);
  const stepList = ["휴대폰 인증을 진행해주세요", "전송된 인증번호를 입력해주세요"];
  const [isFormValid, setIsFormValid] = useState(false);
  const [inputs, setInputs] = useState<InputState>({
    phone: "",
    verificationCode: "",
  });
  const userInfo = useSelector((state: RootState) => state.userInformation.UserInfo);

  useEffect(() => {
    const allFieldsFilled = Object.values(inputs).every((value) => value !== "");

    setIsFormValid(allFieldsFilled);
  }, [inputs]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    // 입력값을 상태에 반영
    setInputs((prev) => ({ ...prev, [id]: value }));
  };

  const handleConfirm = async () => {
    const isValid = await fetchVerifySmsCode();

    if (!isValid) {
      return;
    }

    dispatch(
      editUserInformation({
        ...userInfo,
        phone: inputs.phone,
      })
    );

    navigate("/userupdate");
  };

  // 인증번호 전송
  const handleSendVerificationCode = async () => {
    try {
      const formattedValue = inputs.phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
      const response = await userApi.fetchSendSmsValidation(formattedValue);
      if (response.status === 200) {
        alert("인증 코드가  발송되었습니다. 확인해주세요.");
      }
    } catch (error) {
      console.error("Error sending verification code:", error);
      alert("인증 코드 발송 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
    setStep(1);
  };

  const fetchVerifySmsCode = async () => {
    try {
      const formattedValue = inputs.phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
      const response = await userApi.fetchVerifySmsCode(formattedValue, inputs.verificationCode!);

      if (response.status === 200) {
        return true;
      }
    } catch (error) {
      alert("인증번호를 다시 확인해주세요");
      return false;
    }
  };

  return (
    <div className="w-full min-h-screen p-5 pb-8 bg-white flex flex-col justify-between">
      <div className="flex flex-col">
        <div className="flex items-center mt-[0.14rem]">
          <IoIosArrowBack
            onClick={() => {
              navigate("/userupdate");
            }}
            className="text-2xl"
          />
        </div>
        <div className="grid gap-5">
          <div className="mt-16 text-2xl font-semibold">
            <p>{stepList[step]}</p>
          </div>

          <div className="grid gap-3">
            {step > 0 && (
              <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}>
                <VerificationCodeInput
                  labelName="인증번호 입력"
                  name={inputs.verificationCode}
                  onChange={handleChange}
                />
              </div>
            )}

            <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}>
              <PhoneInput
                labelName="휴대폰 번호"
                name={inputs.phone}
                onChange={handleChange}
                handleSendVerificationCode={handleSendVerificationCode}
              />
            </div>
          </div>
        </div>
      </div>
      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold ${
          isFormValid ? "bg-[#1429A0]" : "bg-gray-300"
        }`}
        onClick={() => {
          handleConfirm();
        }}
        disabled={!isFormValid}>
        확인
      </button>
    </div>
  );
};

export default UserPhoneUpdate;
