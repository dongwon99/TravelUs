import { TextField } from "@mui/material";
import React from "react";

interface Props {
  birthdate: string;
  setBirthdate: (birthdate: string) => void;
}

const BirthDateInputMui = ({ birthdate, setBirthdate }: Props) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          padding: "10px 0",
          fontSize: "20px",
          fontWeight: "bold",
        },
      }}
      id="birthdate"
      label="생년월일"
      value={birthdate}
      variant="standard"
      autoComplete="off"
      onChange={(e) => setBirthdate(e.target.value)}
      inputMode="none"
    />
  );
};

export default BirthDateInputMui;
