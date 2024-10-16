import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router";
import { RiHome5Line } from "react-icons/ri";
import { GoHome } from "react-icons/go";
import { GoHomeFill } from "react-icons/go";
import { IoPeople } from "react-icons/io5";
import { MdOutlineEventNote } from "react-icons/md";
import { GoGraph } from "react-icons/go";
import { setCurrentFooterMenu } from "../../redux/accountSlice";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";

const Footer = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const currentMenu = useSelector((state: RootState) => state.account.currentFooterMenu);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  const handleMenuHome = () => {
    navigate("/");
    dispatch(setCurrentFooterMenu("홈"));
  };

  const handleMenuExchange = () => {
    navigate("/meetingaccountlist");
    dispatch(setCurrentFooterMenu("모임통장"));
  };

  const handleMenuAccountDiary = () => {
    navigate("/accountbookdetail");
    dispatch(setCurrentFooterMenu("머니로그"));
  };

  const handleMenuConversation = () => {
    navigate("/exchangerate");
    dispatch(setCurrentFooterMenu("환율"));
  };

  return (
    <div className="w-full h-[4.7rem] p-3 pb-5 bg-white border-t fixed bottom-0 flex justify-around items-center z-50">
      <button
        className={`flex flex-col items-center ${currentMenu === "홈" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuHome()}>
        {currentMenu === "홈" ? <GoHomeFill className="text-2xl" /> : <GoHome className="text-2xl" />}
        <p className="text-sm font-medium">홈</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "모임통장" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuExchange()}>
        <IoPeople className="text-2xl" />
        <p className="text-sm font-medium">모임통장</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "머니로그" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuAccountDiary()}>
        <MdOutlineEventNote className="text-2xl" />
        <p className="text-sm font-medium">머니로그</p>
      </button>
      <button
        className={`flex flex-col items-center ${currentMenu === "환율" ? "" : "text-[#9E9E9E]"} duration-200`}
        onClick={() => handleMenuConversation()}>
        <GoGraph className="text-2xl" />
        <p className="text-sm font-medium">환율</p>
      </button>
    </div>
  );
};

export default Footer;
