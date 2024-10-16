import React, { useState } from "react";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { MeetingAccountInfo } from "../../types/account";

interface DropdownInputProps {
  meetingAccountList: MeetingAccountInfo[];
  accountNo: string;
  setAccountNo: (accountNo: string) => void;
}

const DropdownInput: React.FC<DropdownInputProps> = ({ meetingAccountList, accountNo, setAccountNo }) => {
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
      <InputLabel id="demo-simple-select-filled-label">모임 선택</InputLabel>
      <Select
        labelId="demo-simple-select-filled-label"
        id="demo-simple-select-filled"
        value={accountNo}
        onChange={(e) => setAccountNo(e.target.value)}
        MenuProps={{
          PaperProps: {
            style: {
              maxHeight: 48 * 3.5,
              borderRadius: "5px",
            },
          },
        }}>
        {meetingAccountList.map((account, index) => (
          <MenuItem value={account.groupAccountNo} key={index}>
            {account.groupName}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default DropdownInput;
