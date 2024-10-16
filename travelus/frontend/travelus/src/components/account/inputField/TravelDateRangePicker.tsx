import React, { useEffect, useState } from "react";
import { Dayjs } from "dayjs";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";

interface TravelDateRangeInputProps {
  schedule: { startDate: Dayjs | null; endDate: Dayjs | null };
  onStartChange: (startDate: Dayjs) => void;
  onEndChange: (endDate: Dayjs) => void;
}

const TravelDateRangePicker: React.FC<TravelDateRangeInputProps> = ({ schedule, onStartChange, onEndChange }) => {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DemoContainer components={["DatePicker", "DatePicker"]}>
        <div className="flex space-x-2">
          <DatePicker
            label="출발일"
            value={schedule.startDate && schedule.startDate}
            onChange={(newValue) => {
              if (newValue) {
                onStartChange(newValue);
              }
            }}
          />
          <DatePicker
            label="도착일"
            value={schedule.endDate && schedule.endDate}
            onChange={(newValue) => {
              if (newValue) {
                onEndChange(newValue);
              }
            }}
          />
        </div>
      </DemoContainer>
    </LocalizationProvider>
  );
};

export default TravelDateRangePicker;
