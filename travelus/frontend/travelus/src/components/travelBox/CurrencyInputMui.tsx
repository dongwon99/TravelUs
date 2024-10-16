import React from "react";
import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import TypeSelect from "../account/inputField/TypeSelect";
import { currencyTypeList } from "../../types/exchange";

interface Props {
  currency: string;
  setCurrency: (type: string) => void;
}

const CurrencyInputMui = ({ currency, setCurrency }: Props) => {
  return (
    <FormControl
      variant="standard"
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          padding: "16px 0 4px 0",
          fontSize: "20px",
          fontWeight: "bold",
        },
      }}>
      <InputLabel id="meetingType">통화</InputLabel>
      <Select
        labelId="meetingType"
        id="meetingType"
        label="meetingType"
        value={currency}
        onChange={(e) => setCurrency(e.target.value)}
        MenuProps={{
          PaperProps: {
            sx: {
              "& .MuiInputBase-root-MuiInput-root-MuiSelect-root": {
                backgroundColor: "#FFFFFF",
              },
              "& .MuiMenuItem-root.Mui-selected": {
                backgroundColor: "#F2F3F5",
              },
            },
          },
        }}>
        {currencyTypeList.map((type, index) => (
          <MenuItem
            value={type.value}
            sx={{
              fontWeight: "bold",
            }}>
            {type.text}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default CurrencyInputMui;
