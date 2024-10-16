import React, { useEffect, useRef } from "react";
import { TextField } from "@mui/material";

interface ResidentNumberInputProps {
  stepInfo: { currentStep: number; closeStep: number };
  residentNumber: string;
  onChange: (number: string) => void;
}

const ResidentNumberInput: React.FC<ResidentNumberInputProps> = ({ stepInfo, residentNumber, onChange }) => {
  const residentNumberRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (stepInfo.currentStep === stepInfo.closeStep) {
      if (residentNumberRef.current) {
        residentNumberRef.current.blur();
      }
    }
  }, [stepInfo]);

  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputBase-root": {
          backgroundColor: "white",
        },
        "& .MuiInputBase-input": {
          backgroundColor: "white",
          fontSize: "20px",
          fontWeight: "bold",
          border: "1px solid #9E9E9E",
          borderRadius: "10px",
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
      label="주민등록번호"
      variant="filled"
      value={residentNumber}
      onChange={(e) => onChange(e.target.value)}
      inputProps={{ maxLength: 14 }}
      autoComplete="off"
      inputRef={residentNumberRef}
    />
  );
};

export default ResidentNumberInput;
