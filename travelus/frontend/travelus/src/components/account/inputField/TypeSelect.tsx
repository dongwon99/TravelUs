import { FormControl, InputLabel, MenuItem } from "@mui/material";
import React from "react";
import Select from "@mui/material/Select";

interface TypeInputProps {
  label: string;
  menuList: Array<{ text: string; value: string }>;
  selectedType: string;
  onChange: (meetingType: string) => void;
}

const TypeSelect: React.FC<TypeInputProps> = ({ label, menuList, selectedType, onChange }) => {
  return (
    <FormControl
      variant="filled"
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
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "16px",
        },
        "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
          display: "none",
        },
        "& .MuiSelect-select:focus": {
          backgroundColor: "white",
          borderRadius: "10px",
        },
      }}>
      <InputLabel id="demo-simple-select-filled-label">{label}</InputLabel>
      <Select
        labelId="demo-simple-select-filled-label"
        id="demo-simple-select-filled"
        value={selectedType}
        onChange={(e) => onChange(e.target.value)}>
        {menuList.map((menu, index) => (
          <MenuItem value={menu.value} key={index}>
            {menu.text}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default TypeSelect;
