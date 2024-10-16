import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import AccountListInputMui from "../../components/meetingAccount/AccountListInputMui";
import { accountApi } from "../../api/account";
import { useDispatch } from "react-redux";
import { setindividualAccountNo } from "../../redux/meetingAccountSlice";
import Loading from "../../components/loading/Loading";

const SelectAccountOfMeeting = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [accountNo, setAccountNo] = useState("");
  const [accountList, setAccountList] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const handleNext = () => {
    dispatch(setindividualAccountNo(accountNo));
    navigate("/meeting/create/password/meeting");
  };

  const getAccountList = async () => {
    try {
      setIsLoading(true);
      const response = await accountApi.fetchAllAccountInfo("I");
      setAccountList(response.data);
    } catch (error) {
      console.log("accountApi의 fetchAllAccountInfo : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getAccountList();
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            className="text-2xl"
            onClick={() => {
              navigate("/meeting/create/userinfo");
            }}
          />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-3">
            <div className="flex space-x-2">
              <p className="text-[#0471E9] font-semibold">01</p>
              <p className="font-medium">모임통장 개설</p>
            </div>

            <div className="text-2xl font-semibold">
              <p>
                정산금을 받을
                <br /> 입출금통장을 선택해주세요
              </p>
            </div>
          </div>

          <AccountListInputMui accountNo={accountNo} setAccountNo={setAccountNo} accountList={accountList} />
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default SelectAccountOfMeeting;
