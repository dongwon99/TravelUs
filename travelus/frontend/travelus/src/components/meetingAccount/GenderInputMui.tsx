import { TextField } from "@mui/material";
import React from "react";

interface Props {
  gender: string;
  setGender: (name: string) => void;
}

const GenderInputMui = ({ gender, setGender }: Props) => {
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
      id="gender"
      label="성별"
      value={gender}
      variant="standard"
      autoComplete="off"
      onChange={(e) => setGender(e.target.value)}
      inputMode="none"
    />
  );
};

export default GenderInputMui;
