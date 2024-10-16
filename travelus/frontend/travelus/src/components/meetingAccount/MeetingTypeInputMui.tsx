import React from "react";
import { meetingAccountIconList } from "../../types/account";
import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";

interface Props {
  meetingType: string;
  setMeetingType: (type: string) => void;
}

const MeetingTypeInputMui = ({ meetingType, setMeetingType }: Props) => {
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
      <InputLabel id="meetingType">모임종류</InputLabel>
      <Select
        labelId="meetingType"
        id="meetingType"
        label="meetingType"
        value={meetingType}
        onChange={(e) => setMeetingType(e.target.value)}
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
        {meetingAccountIconList.map((type, index) => (
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

export default MeetingTypeInputMui;
