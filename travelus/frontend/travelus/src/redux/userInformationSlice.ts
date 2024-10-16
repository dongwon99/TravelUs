import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import { SignUpUserInfo, UserInfo } from "../types/userInformation";

export interface UserInformationState {
  SignUpUserInformation: SignUpUserInfo;
  UserInfo: UserInfo;
}

const initialState: UserInformationState = {
  SignUpUserInformation: {
    id: "",
    password: "",
    confirmPassword: "",
    name: "",
    birthday: "",
    phone: "",
    verificationCode: "",
    address: "",
    gender: "",
  },
  UserInfo: {
    name: "",
    birth: "",
    phone: "",
    id: "",
    address: "",
    registerAt: "",
    isExit: false,
    userId: 0,
    gender: "",
    profileImg: "",
  },
};
    
export const userInfoSilce = createSlice({
  name: "userInformation",
  initialState,
  reducers: {
    editSignUpUserInformation: (state, action: PayloadAction<SignUpUserInfo>) => {
      state.SignUpUserInformation = action.payload;
    },
    editUserInformation: (state, action: PayloadAction<UserInfo>) => {
      state.UserInfo = action.payload;
    }
  },
});

export const { editSignUpUserInformation, editUserInformation } = userInfoSilce.actions;

export default userInfoSilce.reducer;