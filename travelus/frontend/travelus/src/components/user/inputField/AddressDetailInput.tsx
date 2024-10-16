import React from "react";
import { TextField } from "@mui/material";
import style from "./DaumPost.module.css";
import { useDaumPostcodePopup } from "react-daum-postcode";

interface AddressDetailInputProps {
  labelName: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  setAddress: (address: string) => void;
}

const AddressDetailInput: React.FC<AddressDetailInputProps> = ({ labelName, name, onChange }) => {
  return (
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
      id="addressDetail"
      label={labelName}
      variant="filled"
      value={name}
      onChange={onChange}
      autoComplete="off"
      // helperText="영문 소문자 6~13자 (숫자 조합 가능)"
    />
  );
};

export default AddressDetailInput;
