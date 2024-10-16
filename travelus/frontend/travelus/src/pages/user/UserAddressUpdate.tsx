import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editUserInformation } from "../../redux/userInformationSlice";
import AddressInput from "../../components/user/inputField/AddressInput";
import AddressDetailInput from "../../components/user/inputField/AddressDetailInput";
import { IoIosArrowBack } from "react-icons/io";

const UserAddressUpdate = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [address, setAddress] = useState<string>("");
  const [addressDetail, setAddressDetail] = useState<string>("");
  const [fullAddress, setFullAddress] = useState<string>("");
  const [step, setStep] = useState<number>(0);
  const [isFormValid, setIsFormValid] = useState(false);
  const userInfo = useSelector((state: RootState) => state.userInformation.UserInfo);

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

  const handleUpdate = async () => {
    dispatch(editUserInformation({ ...userInfo, address: fullAddress }));
    navigate("/userupdate");
  };

  return (
    <div className="w-full min-h-screen p-5 pb-8 bg-white flex flex-col justify-between">
      <div className="flex flex-col space-y-16">
        <IoIosArrowBack
          onClick={() => {
            navigate("/userupdate");
          }}
          className="text-2xl"
        />
        <div className="mt-10 text-2xl font-semibold">
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
        onClick={handleUpdate}
        className={`w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold ${
          isFormValid ? "bg-[#1429A0]" : "bg-gray-300"
        }`}
        disabled={!isFormValid}>
        확인
      </button>
    </div>
  );
};

export default UserAddressUpdate;
