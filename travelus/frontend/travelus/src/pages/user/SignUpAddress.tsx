import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editSignUpUserInformation } from "../../redux/userInformationSlice";
import { userApi } from "../../api/user";
import AddressInput from "../../components/user/inputField/AddressInput";
import AddressDetailInput from "../../components/user/inputField/AddressDetailInput";
import { add } from "date-fns";

interface SignUpAddressProps {
  // Define your props here
}

const SignUpAddress = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [address, setAddress] = useState<string>("");
  const [addressDetail, setAddressDetail] = useState<string>("");
  const [fullAddress, setFullAddress] = useState<string>("");
  const [step, setStep] = useState<number>(0);
  const [isFormValid, setIsFormValid] = useState(false);
  const signUpInformation = useSelector((state: RootState) => state.userInformation.SignUpUserInformation);

  // console.log("signUpInformation", signUpInformation);

  useEffect(() => {
    if (step === 0 && address !== "") {
      setStep(1);
    }
  }, [address, step]);

  useEffect(() => {
    // 모든 필드가 유효한지 확인
    const allFieldsFilled = address !== "" && addressDetail !== "";

    setFullAddress(`${address} ${addressDetail}`);
    setIsFormValid(allFieldsFilled);
  }, [address, addressDetail]);

  const formatBirthDate = (birthDate: string) => {
    const year = birthDate.slice(0, 4);
    const month = birthDate.slice(4, 6);
    const day = birthDate.slice(6, 8);

    return `${year}-${month}-${day}`;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const id = e.target.id;
    if (id === "address") {
      const address = e.target.value;
      setAddress(address);
    } else if (id === "addressDetail") {
      const addressDetail = e.target.value;
      setAddressDetail(addressDetail);
    }
  };

  const handleSignUp = async () => {
    const formData = new FormData();

    formData.append("id", signUpInformation.id);
    formData.append("password", signUpInformation.password);
    formData.append("name", signUpInformation.name);
    formData.append("phone", signUpInformation.phone);
    formData.append("birth", formatBirthDate(signUpInformation.birthday));
    formData.append("address", fullAddress);
    formData.append("gender", signUpInformation.gender);

    console.log("풀주소", fullAddress);
    try {
      const response = await userApi.fetchSignUp(formData);

      if (response.status === 200) {
        console.log("회원가입 성공");
        navigate("/login");
      }
    } catch (error) {
      console.error("회원가입 에러:", error);
    }
  };
    

  return (
    <div className="w-full min-h-screen p-5 bg-white flex flex-col justify-between">
      <div className="flex flex-col space-y-20">
        <div className="mt-16 text-2xl font-semibold">
          <p>주소를 입력해주세요</p>
        </div>

        <div className="flex flex-col space-y-5">
          {step > 0 && (
            <div>
              <AddressDetailInput
                labelName="상세 주소"
                name={addressDetail}
                onChange={handleChange}
                setAddress={setAddressDetail}
              />
            </div>
          )}

          <div className="transition-transform duration-300 ease-in-out translate-y-[3px]">
            <AddressInput labelName="주소" name={address} onChange={handleChange} setAddress={setAddress} />
          </div>
        </div>
      </div>
      <button
        onClick={handleSignUp}
        className={`w-full mb-5 py-3 text-white rounded-lg ${isFormValid ? "bg-[#1429A0]" : "bg-gray-300"}`}
        disabled={!isFormValid}>
        가입하기
      </button>
    </div>
  );
};

export default SignUpAddress;
