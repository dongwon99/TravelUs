import React from "react";
import { MenuItem, Select, InputLabel, FormControl } from "@mui/material";

interface Props {
  currency: string;
  selectCurrency: string;
  setSelectCurrency: (currency: string) => void;
}

const BuyCurrency = ({ currency, selectCurrency, setSelectCurrency }: Props) => {
  return (
    <FormControl
      variant="standard"
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
      }}>
      <InputLabel id="meetingType">결제 통화 선택</InputLabel>
      <Select
        labelId="currency"
        id="currency"
        label="currency"
        value={selectCurrency}
        onChange={(e) => setSelectCurrency(e.target.value)}
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
        <MenuItem
          value="KRW"
          sx={{
            fontWeight: "bold",
          }}>
          원화 (KRW)
        </MenuItem>
        {currency === "" ? (
          <></>
        ) : (
          <MenuItem
            value="KRW"
            sx={{
              fontWeight: "bold",
            }}>
            외화 ({currency})
          </MenuItem>
        )}
      </Select>
    </FormControl>
  );
};

export default BuyCurrency;
