import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { BuyItemInfo, DayHistoryDetail } from "../types/accountBook";

export interface AccountBookState {
  dayHistoryDetail: Array<DayHistoryDetail>;
  buyItems: Array<BuyItemInfo>;
}

const initialState: AccountBookState = {
  dayHistoryDetail: [],
  buyItems: [],
};

export const accountBookSlice = createSlice({
  name: "accountBook",
  initialState,
  reducers: {
    setDayHistoryDetailList: (state, action: PayloadAction<Array<DayHistoryDetail>>) => {
      state.dayHistoryDetail = action.payload;
    },
    setBuyItems: (state, action: PayloadAction<Array<BuyItemInfo>>) => {
      state.buyItems = action.payload;
    },
  }
});

export const { setDayHistoryDetailList, setBuyItems } = accountBookSlice.actions;

export default accountBookSlice.reducer;