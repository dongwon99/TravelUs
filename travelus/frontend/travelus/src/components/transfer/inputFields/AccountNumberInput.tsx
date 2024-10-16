import React from "react";
import { TextField } from "@mui/material";

interface AccountNumberInputProps {
  labelName: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const AccountNumberInput: React.FC<AccountNumberInputProps> = ({ labelName, value, onChange }) => {
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
      type="tel"
      inputMode="numeric"
      label={labelName}
      variant="standard"
      value={value}
      onChange={onChange}
      autoComplete="off"
    />
  );
};

export default AccountNumberInput;
