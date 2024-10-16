import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import React, { useState } from "react";
import { AccountInfoNew } from "../../types/account";

interface Props {
  accountNo: string;
  setAccountNo: (type: string) => void;
  accountList: AccountInfoNew[];
}

const AccountListInputMui = ({ accountNo, setAccountNo, accountList }: Props) => {
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
      <InputLabel id="meetingType">입출금통장 선택</InputLabel>
      <Select
        labelId="meetingType"
        id="meetingType"
        label="meetingType"
        value={accountNo}
        onChange={(e) => setAccountNo(e.target.value)}
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
        {accountList.map((account, index) => (
          <MenuItem
            value={account.accountNo}
            sx={{
              fontWeight: "bold",
            }}>
            튜나뱅크 {account.accountNo}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default AccountListInputMui;
