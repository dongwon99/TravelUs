import { TextField } from "@mui/material";
import React from "react";

const AccountTypeInput = () => {
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
      id="accountNumber"
      label="은행"
      variant="standard"
      value="튜나뱅크"
      autoComplete="off"
      inputMode="none"
    />
  );
};

export default AccountTypeInput;
