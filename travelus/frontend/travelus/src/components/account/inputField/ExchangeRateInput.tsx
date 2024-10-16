import { TextField } from "@mui/material";
import React, { useEffect, useState } from "react";
import { currencyTypeList } from "../../../types/exchange";
import { AiTwotoneExclamationCircle } from "react-icons/ai";

interface ExchangeRateInputProps {
  currencyType: string;
  exchangeRate: number;
  exchangeRateBDP: number;
  currentExchangeRate: number;
  onChange: (exchangeRate: number) => void;
  onChangeBDP: (exchangeRateBDP: number) => void; // 새로운 핸들러 추가
}

const ExchangeRateInput: React.FC<ExchangeRateInputProps> = ({
  currencyType,
  exchangeRate,
  exchangeRateBDP,
  currentExchangeRate,
  onChange,
  onChangeBDP, // 새로운 핸들러 추가
}) => {
  const [currencyTypeSymbol, setCurrencyTypeSymbol] = useState("");

  useEffect(() => {
    let temp = currencyTypeList.find((item) => item.value === currencyType);
    if (temp) {
      setCurrencyTypeSymbol(temp.text.charAt(temp.text.length - 2));
    }
  }, [currencyType]);

  return (
    <div className="grid gap-1">
      <div className="flex justify-end items-center space-x-1">
        <AiTwotoneExclamationCircle />
        <p>현재 {currencyType}의 환율</p>
        <p>:</p>
        <p className="text-[#0471E9] font-semibold">{currentExchangeRate}</p>
        <p className="text-[#565656] font-semibold">{currencyTypeSymbol}</p>
      </div>

      <div className="flex items-center">
        <TextField
          sx={{
            width: "70%",
            "& .MuiInputBase-root": {
              backgroundColor: "white",
            },
            "& .MuiInputBase-input": {
              backgroundColor: "white",
              fontSize: "20px",
              fontWeight: "bold",
              border: "1px solid #9E9E9E",
              borderRadius: "10px",
              textAlign: "right",
            },
            "& .MuiInputLabel-root": {
              color: "#9E9E9E",
              fontSize: "20px",
            },
            "& .MuiInputLabel-shrink": {
              fontSize: "16px",
            },
            "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
              display: "none",
            },
          }}
          id="filled-basic"
          label="희망 환율"
          variant="filled"
          value={exchangeRate}
          onChange={(e) => {
            onChange(Number(e.target.value)); // '희망 환율' 필드에 대한 onChange 핸들러
          }}
          inputProps={{ inputMode: "decimal" }}
          autoComplete="off"
        />

        <span
          className="mx-1 text-gray-400"
          style={{
            fontSize: "42px", // 점의 크기를 키움
            lineHeight: "0", // 수직 정렬을 아래로 내림
            position: "relative",
            top: "5px", // 점을 아래로 내리기 위해 top 조정
            opacity: "1.0", // 색상 투명도를 조절하여 회색빛 효과
          }}
        >
          .
        </span>
        <TextField
          sx={{
            width: "29%",
            "& .MuiInputBase-root": {
              backgroundColor: "white",
            },
            "& .MuiInputBase-input": {
              backgroundColor: "white",
              fontSize: "20px",
              fontWeight: "bold",
              border: "1px solid #9E9E9E",
              borderRadius: "10px",
              textAlign: "right",
            },
            "& .MuiInputLabel-root": {
              color: "#9E9E9E",
              fontSize: "20px",
            },
            "& .MuiInputLabel-shrink": {
              fontSize: "16px",
            },
            "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
              display: "none",
            },
          }}
          id="filled-basic-bdp"
          label="소수점 아래"
          variant="filled"
          value={exchangeRateBDP}
          onChange={(e) => {
            onChangeBDP(Number(e.target.value)); // '소수점 아래' 필드에 대한 onChange 핸들러
          }}
          inputProps={{ inputMode: "decimal" }}
          autoComplete="off"
        />
      </div>
    </div>
  );
};

export default ExchangeRateInput;
