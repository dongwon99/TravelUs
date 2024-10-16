import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import OldUserPasswordInput from "../../components/user/inputField/OldUserPasswordInput";
import UserPasswordInput from "../../components/user/inputField/UserPasswordInput";
import UserPasswordConfirmInput from "../../components/user/inputField/UserPasswordConfirmInput";
import { IoIosArrowBack } from "react-icons/io";

interface InputState {
  oldPassword: string;
  password: string;
  confirmPassword: string;
}

const UserPasswordUpdate = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const stepList = ["기존 비밀번호를 입력해주세요", "새로운 비밀번호를 입력해주세요", "새로운 비밀번호를 확인해주세요"];
  const [isFormValid, setIsFormValid] = useState(false);
  const [inputs, setInputs] = useState<InputState>({
    oldPassword: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({
    oldPassword: false,
    password: false,
    confirmPassword: false,
  });

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const isKorean = /^[가-힣]+$/.test(value);

    switch (id) {
      case "password":
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]).{8,15}$/;
        error = !passwordRegex.test(value);
        break;
      case "confirmPassword":
        error = value !== inputs.password; // 패스워드 확인이 일치하는지 확인
        break;
    }
    return error;
  };

  useEffect(() => {
    const allFieldsFilled = Object.values(inputs).every((value) => value !== "");
    const allFieldsValid = Object.values(errors).every((error) => !error);

    setIsFormValid(allFieldsFilled && allFieldsValid);
  }, [inputs, errors]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    // 입력값을 상태에 반영
    setInputs((prev) => ({ ...prev, [id]: value }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implement phone update logic
  };

  // 새로운 useEffect: 기존 비밀번호 확인 처리
  useEffect(() => {
    if (step === 0 && inputs.oldPassword.length >= 8 && !errors.oldPassword) {
      setStep(1);
    }
  }, [inputs.oldPassword, errors.oldPassword, step]);

  // 새로운 useEffect: 비밀번호 입력 처리
  useEffect(() => {
    if (step === 1 && inputs.password.length >= 8 && inputs.password.length <= 15 && !errors.password) {
      setStep(2);
    }
  }, [inputs.password, errors.password, step]);

  // 비밀번호 확인 처리
  // useEffect(() => {
  //   if (step === 2 && inputs.confirmPassword === inputs.newPassword && !errors.confirmPassword) {
  //     setStep(2);
  //   }
  // }, [inputs.confirmPassword, inputs.newPassword, errors.confirmPassword, step]);

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
        <div className="grid gap-14">
          <div className="mt-16 text-2xl font-semibold">
            <p>{stepList[step]}</p>
          </div>

          <div className="grid gap-3">
            {step > 1 && (
              <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}>
                <UserPasswordConfirmInput
                  labelName="새로운 비밀번호 확인"
                  name={inputs.confirmPassword}
                  onChange={handleChange}
                  error={errors.confirmPassword}
                />
              </div>
            )}
            
            {step > 0 && (
              <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}>
                <UserPasswordInput
                  labelName="새로운 비밀번호"
                  name={inputs.password}
                  onChange={handleChange}
                  error={errors.password}
                />
              </div>
            )}

            <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}>
              <OldUserPasswordInput
                labelName="기존 비밀번호"
                name={inputs.oldPassword}
                onChange={handleChange}
                error={false}
              />
            </div>

            <div className={`transition-transform duration-300 ease-in-out translate-y-[3px]`}></div>
          </div>
        </div>
      </div>
      <button
        className={`w-full py-3 text-white rounded-lg ${isFormValid ? "bg-[#1429A0]" : "bg-gray-300"}`}
        onClick={() => {
          navigate("/");
        }}
        disabled={!isFormValid}>
        확인
      </button>
    </div>
  );
};

export default UserPasswordUpdate;
