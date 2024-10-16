import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editSignUpUserInformation } from "../../redux/userInformationSlice";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../api/user";
import IdInput from "../../components/user/inputField/IdInput";
import UserPasswordInput from "../../components/user/inputField/UserPasswordInput";
import UserPasswordConfirmInput from "../../components/user/inputField/UserPasswordConfirmInput";
import NameInput from "../../components/user/inputField/NameInput";
import BirthdayInput from "../../components/user/inputField/BirthdayInput";
import PhoneInput from "../../components/user/inputField/PhoneInput";
import VerificationCodeInput from "../../components/user/inputField/VerificationCodeInput";
import GenderInput from "../../components/user/inputField/GenderInput";
import { set } from "date-fns";

interface SignUpBasicInformationProps {
  // Define your props here
}

interface InputState {
  id: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
  phone: string;
  verificationCode: string;
  gender: string;
}

const SignUpBasicInformation = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [step, setStep] = useState(0);
  const stepList = [
    "아이디를 입력해주세요",
    "비밀번호를 입력해주세요",
    "비밀번호를 확인해주세요",
    "이름을 알려주세요",
    "생년월일을 알려주세요",
    "성별을 알려주세요",
    "휴대폰 인증을 진행해주세요",
    "전송된 인증번호를 입력해주세요",
  ];

  const [isFormValid, setIsFormValid] = useState(false);
  const [isBasicFormValid, setIsBasicFormValid] = useState(false);
  const [isIdDuplicated, setIsIdDuplicated] = useState(false);

  const [inputs, setInputs] = useState<InputState>({
    id: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
    phone: "",
    verificationCode: "",
    gender: "",
  });

  const [errors, setErrors] = useState({
    id: false,
    password: false,
    confirmPassword: false,
    name: false,
    birthday: false,
    phone: false,
    verificationCode: false,
  });

  useEffect(() => {
    // redux store의 기존 저장되어있던 정보 제거
    dispatch(
      editSignUpUserInformation({
        id: "",
        password: "",
        confirmPassword: "",
        name: "",
        birthday: "",
        phone: "",
        verificationCode: "",
        address: "",
        gender: "",
      })
    );
  }, []);

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const isKorean = /^[가-힣]+$/.test(value);

    switch (id) {
      case "id":
        const idRegex = /^[a-z0-9]{6,13}$/;
        error = !idRegex.test(value);
        break;
      case "password":
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]).{8,15}$/;
        error = !passwordRegex.test(value);
        break;
      case "confirmPassword":
        error = value !== inputs.password; // 패스워드 확인이 일치하는지 확인
        break;
      case "name":
        error = !isKorean || value.trim() === "";
        break;
      case "birthday":
        // 생년월일 유효성 검사
        const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
        error = !dateRegex.test(value);
        break;
    }
    return error;
  };

  const formatBirthday = (value: string) => {
    // Remove any non-digit characters
    const cleanedValue = value.replace(/\D/g, "");

    // Format as YYYY-MM-DD
    if (cleanedValue.length <= 4) {
      return cleanedValue;
    } else if (cleanedValue.length <= 6) {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4)}`;
    } else {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4, 6)}-${cleanedValue.slice(6, 8)}`;
    }
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
    setStep(7);
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

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    const formattedValue = id === "birthday" ? formatBirthday(value) : value;

    setInputs((prev) => ({ ...prev, [id]: formattedValue }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  useEffect(() => {
    const requiredFields: (keyof typeof errors)[] = ["id", "password", "confirmPassword", "name", "birthday"];

    // 기본 정보 필드가 유효한지 확인
    const basicFieldsValid = requiredFields.every((field) => !errors[field]);
    // inputs 객체에서 해당 필드들이 모두 채워졌는지 확인
    const basicFieldsFilled = requiredFields.every((field) => inputs[field] !== "");

    // 모든 필드가 유효한지 확인
    const allFieldsValid = Object.values(errors).every((error) => !error);
    const allFieldsFilled = Object.values(inputs).every((value) => value !== "");

    setIsBasicFormValid(basicFieldsValid && basicFieldsFilled);
    setIsFormValid(allFieldsValid && allFieldsFilled);
  }, [errors, inputs]);

  // 새로운 useEffect: 비밀번호 입력 처리
  useEffect(() => {
    if (step === 1 && inputs.password.length >= 8 && inputs.password.length <= 15 && !errors.password) {
      setStep(2);
    }
  }, [inputs.password, errors.password, step]);

  // 비밀번호 확인 처리
  useEffect(() => {
    if (step === 2 && inputs.confirmPassword === inputs.password && !errors.confirmPassword) {
      setStep(3);
    }
  }, [inputs.confirmPassword, inputs.password, errors.confirmPassword, step]);

  // 이름 입력 처리
  useEffect(() => {
    if (step === 3 && inputs.name.length >= 2 && !errors.name) {
      setStep(4);
    }
  }, [inputs.name, errors.name, step]);

  // 생일 입력 처리
  useEffect(() => {
    if (step === 4 && inputs.birthday.length >= 10) {
      setStep(5);
    }
  }, [inputs.birthday, step]);

  const handleNext = async () => {
    try {
      const isVerified = await fetchVerifySmsCode();
      if (!isVerified) {
        return;
      }

    dispatch(
      editSignUpUserInformation({
        id: inputs.id,
        password: inputs.password,
        confirmPassword: inputs.confirmPassword,
        name: inputs.name,
        birthday: inputs.birthday.replace(/-/g, ""),
        phone: inputs.phone,
        verificationCode: inputs.verificationCode,
        address: "",
        gender: inputs.gender,
      })
    );

    navigate("/signup/address");
    } catch (error) {
      console.error("Error verifying SMS code:", error);
    }
  }

  const handleIsIdDuplicated = async () => {
    console.log("중복확인");
    try {
      const response = await userApi.fetchValidateId(inputs.id);
      if (response.data.status === "FAIL") {
        setIsIdDuplicated(true);
      } else if (response.data.status === "SUCCESS") {
        setIsIdDuplicated(false);
        setStep(1);
      }
    }
    catch (error) {
      console.error("Error validating id:", error);
    }
  };

  return (
    <div className="w-full min-h-screen p-5 bg-white flex flex-col justify-between">
      <div className="flex flex-col space-y-5">
        <div className="grid gap-8">
          <div className="mt-16 text-2xl font-semibold">
            <p>{stepList[step]}</p>
          </div>

          <div className="grid gap-3">
            {step < 6 ? (
              <>
                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 4 ? "translate-y-[3px]" : "translate-y-0"
                  }`}>
                  {step > 4 && (
                    <GenderInput
                      name={inputs.gender}
                      onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                        const selectedGender = event.target.value; // 선택된 값은 value로 가져옴
                        setInputs((prev) => ({ ...prev, gender: selectedGender })); // gender 값 업데이트
                      }}
                    />
                  )}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 3 ? "translate-y-[3px]" : "translate-y-0"
                  }`}>
                  {step > 3 && <BirthdayInput labelName="생년월일" name={inputs.birthday} onChange={handleChange} />}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 2 ? "translate-y-[3px]" : "translate-y-0"
                  }`}>
                  {step > 2 && (
                    <NameInput labelName="이름" name={inputs.name} onChange={handleChange} error={errors.name} />
                  )}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 1 ? "translate-y-0" : "translate-y-[3px]"
                  }`}>
                  {step > 1 && (
                    <UserPasswordConfirmInput
                      labelName="비밀번호 확인"
                      name={inputs.confirmPassword}
                      error={errors.confirmPassword}
                      onChange={handleChange}
                    />
                  )}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 0 ? "translate-y-0" : "translate-y-[3px]"
                  }`}>
                  {step > 0 && (
                    <UserPasswordInput
                      labelName="비밀번호"
                      name={inputs.password}
                      error={errors.password}
                      onChange={handleChange}
                    />
                  )}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step === 0 ? "translate-y-0" : "translate-y-[3px]"
                  }`}>
                  <IdInput
                    labelName="아이디"
                    name={inputs.id}
                    error={errors.id}
                    isIdDuplicated={isIdDuplicated}
                    onChange={handleChange}
                    handleIsIdDuplicated={handleIsIdDuplicated}
                  />
                </div>
              </>
            ) : (
              <>
                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 6 ? "translate-y-0" : "translate-y-[3px]"
                  }`}>
                  {step > 6 && (
                    <VerificationCodeInput
                      labelName="인증번호 입력"
                      name={inputs.verificationCode}
                      onChange={handleChange}
                    />
                  )}
                </div>

                <div
                  className={`transition-transform duration-300 ease-in-out ${
                    step > 5 ? "translate-y-[3px]" : "translate-y-0"
                  }`}>
                  {step > 5 && (
                    <PhoneInput
                      labelName="휴대폰 번호"
                      name={inputs.phone}
                      onChange={handleChange}
                      handleSendVerificationCode={handleSendVerificationCode}
                    />
                  )}
                </div>
              </>
            )}
          </div>
        </div>
      </div>

      <div className="py-5">
        {step < 6 ? (
          <>
            <button
              className={`w-full py-3 text-white rounded-lg ${isBasicFormValid ? "bg-[#1429A0]" : "bg-gray-300"}`}
              onClick={() => {
                setStep(6);
              }}
              disabled={!isBasicFormValid}>
              다음
            </button>
          </>
        ) : (
          <>
            <button
              className={`w-full py-3 text-white rounded-lg ${isFormValid ? "bg-[#1429A0]" : "bg-gray-300"}`}
              onClick={handleNext}
              disabled={!isFormValid}>
              다음
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default SignUpBasicInformation;
