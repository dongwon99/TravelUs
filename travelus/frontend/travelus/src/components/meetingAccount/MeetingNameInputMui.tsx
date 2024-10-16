import { TextField } from "@mui/material";
import React from "react";

interface Props {
  meetingName: string;
  setMeetingName: (name: string) => void;
  onInputComplete: () => void;
}

const MeetingNameInputMui = ({ meetingName, setMeetingName, onInputComplete }: Props) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          padding: "10px 0",
          fontSize: "20px",
          fontWeight: "bold",
        },
      }}
      id="meetingname"
      label="모임명"
      value={meetingName}
      variant="standard"
      autoComplete="off"
      onChange={(e) => setMeetingName(e.target.value)}
      onBlur={onInputComplete} // 입력이 완료되면 호출
    />
  );
};

export default MeetingNameInputMui;
