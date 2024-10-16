import { TextField } from "@mui/material";
import React from "react";

interface Props {
  labelName: string;
  date: string;
  setDate: (date: string) => void;
}

const BuyDateInputMui = ({ labelName, date, setDate }: Props) => {
  // 날짜 형식에 맞춰 하이픈을 추가하는 함수
  const formatDateWithHyphen = (value: string) => {
    // 숫자만 남기고 처리
    const cleanedValue = value.replace(/\D/g, "");

    // 길이에 따라 하이픈 추가
    if (cleanedValue.length <= 4) {
      return cleanedValue; // 연도만 입력
    } else if (cleanedValue.length <= 6) {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4)}`; // 연도-월
    } else {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4, 6)}-${cleanedValue.slice(6, 8)}`; // 연도-월-일
    }
  };

  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const formattedDate = formatDateWithHyphen(e.target.value);
    setDate(formattedDate);
  };

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
      id="date"
      label={labelName}
      variant="standard"
      value={date}
      helperText="ex) 20240101"
      onChange={handleDateChange}
      autoComplete="off"
    />
  );
};

export default BuyDateInputMui;
