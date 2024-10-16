import React from "react";
import { TextField } from "@mui/material";

interface PhoneInputProps {
  labelName: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleSendVerificationCode: () => void;
}

const PhoneInput: React.FC<PhoneInputProps> = ({ labelName, name, onChange, handleSendVerificationCode }) => {
  return (
    <div className="flex justify-between items-start space-x-3">
      <TextField
        sx={{
          width: "100%",
          backgroundColor: "white",
          borderRadius: "10px",
          "& .MuiInputBase-root": {
            backgroundColor: "white",
            height: "100%",
            borderRadius: "inherit",
          },
          "& .MuiInputBase-input": {
            backgroundColor: "white",
            fontSize: "18px",
            fontWeight: "bold",
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
        id="phone"
        label={labelName}
        variant="filled"
        value={name}
        helperText="숫자만 입력해주세요."
        onChange={onChange}
        autoComplete="off"
      />
      <button
        onClick={handleSendVerificationCode}
        className="w-24 h-[3.9rem] px-3 p-2 bg-[#1429A0] border rounded-lg text-white text-sm">
        인증번호 전송
      </button>
    </div>
  );
};

export default PhoneInput;
