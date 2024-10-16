import React from "react";
import { TextField } from "@mui/material";

interface NameInputProps {
  labelName: string;
  name: string;
  error: boolean;
  isIdDuplicated: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleIsIdDuplicated: () => void;
}

const IdInput: React.FC<NameInputProps> = ({
  labelName,
  name,
  error,
  isIdDuplicated,
  onChange,
  handleIsIdDuplicated,
}) => {
  return (
    <div className="flex justify-between items-start space-x-3">
      <TextField
        className="w-full"
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
            border: (theme) => `1px solid ${error || isIdDuplicated ? theme.palette.error.main : "#9E9E9E"}`,
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
        id="id"
        label={labelName}
        variant="filled"
        value={name}
        onChange={onChange}
        autoComplete="off"
        helperText={`${isIdDuplicated ? " 중복된 아이디입니다." : "영문 소문자 6~13자 (숫자 조합 가능)"}`}
        error={error}
      />
      <button
        onClick={handleIsIdDuplicated}
        className="w-24 h-[3.9rem] px-3 p-2 bg-[#1429A0] border rounded-lg text-white text-sm">
        중복확인
      </button>
    </div>
  );
};

export default IdInput;
