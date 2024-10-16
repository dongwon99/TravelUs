import React from "react";
import { TextField } from "@mui/material";

interface NameInputProps {
  labelName: string;
  name: string;
  onChange: (name: string) => void;
}

const NameInput: React.FC<NameInputProps> = ({ labelName, name, onChange }) => {
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
      label={labelName}
      variant="filled"
      value={name}
      onChange={(e) => onChange(e.target.value)}
      autoComplete="off"
    />
  );
};

export default NameInput;
