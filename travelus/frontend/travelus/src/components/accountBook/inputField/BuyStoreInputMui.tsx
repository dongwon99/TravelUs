import { TextField } from "@mui/material";
import React from "react";

interface Props {
  buyStore: string;
  setBuyStore: (buyStore: string) => void;
}

const BuyStoreInputMui = ({ buyStore, setBuyStore }: Props) => {
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
      label="사용처"
      variant="standard"
      autoComplete="off"
      value={buyStore}
      onChange={(e) => setBuyStore(e.target.value)}
    />
  );
};

export default BuyStoreInputMui;
