import { TextField } from "@mui/material";
import React from "react";

interface Props {
  paid: string;
  setPaid: (paid: string) => void;
}

const BuyPaidInputMui = ({ paid, setPaid }: Props) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "18px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          padding: "10px 0",
          fontSize: "18px",
        },
      }}
      id="filled-basic"
      label="사용금액"
      type="number"
      variant="standard"
      autoComplete="off"
      value={paid}
      onChange={(e) => setPaid(e.target.value)}
    />
  );
};

export default BuyPaidInputMui;
