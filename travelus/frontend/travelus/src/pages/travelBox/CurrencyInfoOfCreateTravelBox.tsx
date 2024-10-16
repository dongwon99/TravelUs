import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import CurrencyInputMui from "../../components/travelBox/CurrencyInputMui";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { setTravelboxInfo } from "../../redux/meetingAccountSlice";

const CurrencyInfoOfCreateTravelBox = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [currency, setCurrency] = useState("");
  const travelboxInfo = useSelector((state: RootState) => state.meetingAccount.travelboxInfo);
  const [isValid, setIsValid] = useState(false);

  const handleNext = () => {
    const updatedTravelboxInfo = { ...travelboxInfo, currencyCode: currency };
    dispatch(setTravelboxInfo(updatedTravelboxInfo));
    navigate("/meeting/create/password/travelbox");
  };

  useEffect(() => {
    if (currency !== "") {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [currency]);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">02</p>
              <p className="font-medium">외화저금통 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>통화를</p>
              <p>선택해주세요</p>
            </div>
          </div>

          <CurrencyInputMui currency={currency} setCurrency={setCurrency} />
        </div>
      </div>

      <button
        className={`w-full h-14 text-lg rounded-xl tracking-wide ${
          isValid ? "text-white bg-[#1429A0]" : "text-[#565656] bg-[#E3E4E4]"
        } `}
        onClick={() => handleNext()}
        disabled={!isValid}>
        다음
      </button>
    </div>
  );
};

export default CurrencyInfoOfCreateTravelBox;
