import React from "react";
import { TextField } from "@mui/material";

interface UserPasswordInputProps {
  labelName: string;
  name: string;
  error: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const UserPasswordInput: React.FC<UserPasswordInputProps> = ({ labelName, name, error, onChange }) => {
  return (
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
          border: (theme) => `1px solid ${error ? theme.palette.error.main : "#9E9E9E"}`,
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
      id="password"
      type="password"
      label={labelName}
      variant="filled"
      value={name}
      onChange={onChange}
      autoComplete="off"
      helperText="8~15자 | 특수문자, 영문 소/대문자, 숫자 1개 이상씩 조합"
      error={error}
    />
  );
};

export default UserPasswordInput;
