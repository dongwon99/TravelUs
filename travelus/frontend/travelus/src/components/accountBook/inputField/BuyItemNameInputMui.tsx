import { TextField } from "@mui/material";
import React from "react";

interface Props {
  itemName: string;
  setItemName: (birthdate: string) => void;
}

const BuyItemNameInputMui = ({ itemName, setItemName }: Props) => {
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
      label="상품명"
      variant="standard"
      autoComplete="off"
      value={itemName}
      onChange={(e) => setItemName(e.target.value)}
    />
  );
};

export default BuyItemNameInputMui;
