import { useNavigate } from "react-router";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { FaUserFriends, FaBriefcase, FaHeart } from "react-icons/fa"; // 필요한 추가 아이콘 임포트
import { IoSchool } from "react-icons/io5";
import { IoHome } from "react-icons/io5";
import { AccountInfo } from "../../types/account";
import { useEffect, useState } from "react";
import { accountApi } from "../../api/account";

interface Props {
  index: number;
  account: any;
  accountId: number;
}

const JoinedMeetingAccount = ({ index, account }: Props) => {
  const navigate = useNavigate();

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatAccountNumber = (accountNo: string) => {
    // 계좌번호를 각 4자리씩 나누고 '-'로 연결
    return accountNo.replace(/(\d{3})(\d{4})(\d{4})(\d{5})/, "$1-$2-$3-$4");
  };

  const getIcon = (iconName: string) => {
    // 아이콘별 배경색을 정의하는 객체
    const iconBackgrounds: Record<string, string> = {
      airPlane: "bg-[#638ee4]",
      friend: "bg-[#F5E198]",
      family: "bg-[#FFB555]",
      lover: "bg-[#EB8CA2]",
      job: "bg-[#95DBC1]",
      default: "bg-[#638ee4]", // 기본 배경색
    };

    // 해당 아이콘의 배경색을 가져오고, 없으면 기본값 사용
    const backgroundClass = iconBackgrounds[iconName] || iconBackgrounds.default;

    const containerClasses = `w-10 h-10 ${backgroundClass} rounded-full flex justify-center items-center text-white`;
    const iconClasses = "w-6 h-6"; // 아이콘 자체 크기를 줄이기 위한 클래스

    let IconComponent;

    switch (iconName) {
      case "airPlane":
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
      case "friend":
        IconComponent = <FaUserFriends className={iconClasses} />;
        break;
      case "family":
        IconComponent = <IoHome className={iconClasses} />;
        break;
      case "lover":
        IconComponent = <FaHeart className={iconClasses} />;
        break;
      case "job":
        IconComponent = <FaBriefcase className={iconClasses} />;
        break;
      default:
        IconComponent = <PiAirplaneTiltFill className={iconClasses} />;
        break;
    }

    return <span className={containerClasses}>{IconComponent}</span>;
  };

  return (
    <div
      key={index}
      onClick={() => {
        navigate(`/meetingaccount/${account.groupId}`);
      }}
      className="w-full">
      <div className="flex items-center space-x-4">
        <p>{getIcon(account.icon)}</p>
        <div className="w-full flex flex-col">
          <p className="text-zinc-600 font-bold text-[0.75rem]">{account.groupName}</p>
          <div className="w-full flex justify-between">
            <p className="font-bold">{formatCurrency(account.moneyBoxDtoList[0].balance)}원</p>
          </div>
          <div className="text-sm w-full flex justify-between">
            {account.moneyBoxDtoList.length > 1 ? (
              <div className="w-full flex justify-between items-center">
                <p className="text-zinc-500 text-[0.83rem]">외화저금통</p>
                <div className="text-[1rem] font-semibold flex space-x-1">
                  <p>{formatCurrency(account.moneyBoxDtoList[1].balance)}</p>
                  <p>{account.moneyBoxDtoList[1].currencyCode}</p>
                </div>
              </div>
            ) : (
              <div className="w-full flex justify-between items-center"></div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default JoinedMeetingAccount;
