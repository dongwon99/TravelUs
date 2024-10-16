import React from "react";
import { TextField } from "@mui/material";

interface NameInputProps {
  labelName: string;
  name: string;
  error: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const NameInput: React.FC<NameInputProps> = ({ labelName, name, error, onChange }) => {
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
      id="name"
      label={labelName}
      variant="filled"
      value={name}
      helperText="한글 2~8자"
      onChange={onChange}
      autoComplete="off"
      error={error}
    />
  );
};

export default NameInput;
