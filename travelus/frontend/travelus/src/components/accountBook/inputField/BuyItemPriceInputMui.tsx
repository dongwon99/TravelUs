import { TextField } from "@mui/material";
import React from "react";

interface Props {
  price: string;
  setPrice: (price: string) => void;
}

const BuyItemPriceInputMui = ({ price, setPrice }: Props) => {
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
      label="금액"
      variant="standard"
      autoComplete="off"
      value={price}
      onChange={(e) => setPrice(e.target.value)}
    />
  );
};

export default BuyItemPriceInputMui;
