import React from "react";
import { TextField } from "@mui/material";
import style from "./DaumPost.module.css";
import { useDaumPostcodePopup } from "react-daum-postcode";

interface AddressInputProps {
  labelName: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  setAddress: (address: string) => void;
}

interface DaumPostcodeData {
  address: string; // 도로명 주소
  sido: string; // 시/도
  sigungu: string; // 시/군/구
  bname: string; // 법정동명
  buildingName: string; // 건물명
  addressType: string; // 주소 타입 (R: 도로명, J: 지번)
  zonecode: string; // 우편번호
}

const AddressInput: React.FC<AddressInputProps> = ({ labelName, name, onChange, setAddress }) => {
  const postcodeScriptUrl = "https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
  const open = useDaumPostcodePopup(postcodeScriptUrl);

  const handleComplete = (data: DaumPostcodeData) => {
    setAddress(""); // 주소를 초기화

    let fullAddress = data.address;
    let extraAddress = "";

    // 도로명 주소일 경우
    if (data.addressType === "R") {
      if (data.bname !== "") {
        extraAddress += data.bname;
      }
      if (data.buildingName !== "") {
        extraAddress += extraAddress !== "" ? `, ${data.buildingName}` : data.buildingName;
      }
      fullAddress += extraAddress !== "" ? ` (${extraAddress})` : "";
    }

    // localAddress를 따로 사용하지 않고, fullAddress 그대로 사용
    setAddress(fullAddress.trim()); // setAddress를 호출하여 부모 컴포넌트의 상태를 업데이트
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };

  return (
    <div className="flex flex-col space-y-3">
      <TextField
        className="w-full"
        sx={{
          width: "100%",
          backgroundColor: "#F3F4F6",
          borderRadius: "10px",
          "& .MuiInputBase-root": {
            backgroundColor: "white",
            height: "100%",
            borderRadius: "inherit",
          },
          "& .MuiInputBase-input": {
            backgroundColor: "white",
            fontSize: "16px",
            fontWeight: "bold",
            // border: (theme) => `1px solid ${error ? theme.palette.error.main : "#9E9E9E"}`,
            border: "1px solid #9E9E9E",
            borderRadius: "10px",
          },
          "& .MuiInputLabel-root": {
            color: "#9E9E9E",
            fontSize: "20px",
          },
          "& .MuiInputLabel-shrink": {
            fontSize: "16px",
          },
          "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
            display: "none",
          },
        }}
        id="address"
        label={labelName}
        variant="filled"
        value={name}
        onChange={onChange}
        autoComplete="off"
        // helperText="영문 소문자 6~13자 (숫자 조합 가능)"
      />
      <button onClick={handleClick} className="w-full h-12 px-3 p-2 bg-[#1429A0] border rounded-lg text-white text-sm">
        주소검색
      </button>
    </div>
  );
};

export default AddressInput;
