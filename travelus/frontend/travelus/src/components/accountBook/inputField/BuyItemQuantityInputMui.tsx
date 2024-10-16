import { TextField } from "@mui/material";
import React from "react";

interface Props {
  quantity: number;
  setQuantity: (quantity: number) => void;
}

const BuyItemQuantityInputMui = ({ quantity, setQuantity }: Props) => {
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
      label="수량"
      variant="standard"
      autoComplete="off"
      type="tel"
      value={quantity === 0 ? "" : quantity}
      onChange={(e) => setQuantity(Number(e.target.value))}
    />
  );
};

export default BuyItemQuantityInputMui;
