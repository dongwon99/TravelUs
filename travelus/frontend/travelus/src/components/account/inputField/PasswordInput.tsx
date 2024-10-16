import React from "react";
import { TextField } from "@mui/material";

interface PasswordInputProps {
  maskedPassword: string;
  onKeyboardOpen: () => void;
}

const PasswordInput: React.FC<PasswordInputProps> = ({ maskedPassword, onKeyboardOpen }) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputBase-root": {
          backgroundColor: "white",
        },
        "& .MuiInputBase-input": {
          backgroundColor: "white",
          fontSize: "20px",
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
      id="filled-basic"
      label="계좌비밀번호"
      variant="filled"
      value={maskedPassword}
      onClick={() => onKeyboardOpen()}
      inputProps={{ maxLength: 4, readOnly: true }}
    />
  );
};

export default PasswordInput;
