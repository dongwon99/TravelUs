import { configureStore } from "@reduxjs/toolkit";
import accountReducer from "./accountSlice";
import accountBookReducer from "./accountBookSlice";
import userInformationReducer from "./userInformationSlice";
import meetingAccountReducer from "./meetingAccountSlice";

export const store = configureStore({
  reducer: {
    account: accountReducer,
    accountBook: accountBookReducer,
    userInformation: userInformationReducer,
    meetingAccount: meetingAccountReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
