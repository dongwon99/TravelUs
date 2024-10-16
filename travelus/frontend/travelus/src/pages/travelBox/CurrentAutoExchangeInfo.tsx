import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate, useParams } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { accountApi } from "../../api/account";
import Loading from "../../components/loading/Loading";
import { AutoExchangeInfo } from "../../types/account";
import { setMeetingAccountInfo } from "../../redux/meetingAccountSlice";
import { RiExchangeDollarLine } from "react-icons/ri";
import { FaCoins } from "react-icons/fa";

const CurrentAutoExchangeInfo = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const { groupId } = useParams();
  const [type, setType] = useState("");
  const [autoExchangeInfo, setAutoExchangeInfo] = useState<AutoExchangeInfo | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const meetingAccountInfo = useSelector((state: RootState) => state.meetingAccount.meetingAccounInfo);

  const guideData = [
    {
      type: "AUTO",
      text: ["사용자 설정", "자동환전", "환율, 금액을 직접 선택해 자동환전해요"],
      img: "userIcon_blue",
    },
    {
      type: "NOW",
      text: ["즉시", "자동환전", "모임통장 잔액을", "현재 환율로 즉시 자동환전해요"],
      img: "immediatelyExchange",
    },
    {
      type: "NONE",
      text: ["자동환전", "안 할래요", "직접 환전을 통해서만 환전해요"],
      img: "directlyExchange",
    },
  ];

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const handleNext = () => {
    const updatedMeetingAccountInfo = { ...meetingAccountInfo, groupId: Number(groupId) };
    dispatch(setMeetingAccountInfo(updatedMeetingAccountInfo));
    navigate("/travelbox/create/type", {
      state: {
        exchangeType: type,
        currencyCode: location.state.currencyCode,
        groupId: location.state.groupId,
      },
    });
  };

  // 자동환전 타입
  const fetchGroupInfo = async () => {
    try {
      setIsLoading(true);
      const response = await accountApi.fetchSpecificMeetingAccount(Number(groupId));
      setType(response.data.exchangeType);
    } catch (error) {
      console.log("", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 사용자 설정 자동환전의 환율 정보 조회
  const fetchAutoExchangeType = async () => {
    try {
      setIsLoading(true);
      const response = await accountApi.fetchAutoExchangeType(Number(groupId));
      setAutoExchangeInfo(response.data);
    } catch (error) {
      console.log("accountApi의 fetchAutoExchangeType : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchGroupInfo();
  }, []);

  useEffect(() => {
    if (type === "AUTO") {
      fetchAutoExchangeType();
    }
  }, [type]);

  if (isLoading || type === "") {
    <Loading />;
  }

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
        </div>

        <div className="grid gap-10">
          <p className="text-2xl font-semibold">
            설정된 <span className="text-[#1429A0]">자동환전</span> 정보를
            <br />
            확인하세요
          </p>

          <div>
            <div className="flex flex-col space-y-5">
              {guideData.map((data, index) =>
                data.type === type ? (
                  <>
                    <div
                      className={`w-full p-4 py-6 rounded-lg bg-[#eef4ff] flex flex-col transition-all duration-300 ease-in-out border-2 border-transparent`}>
                      <div className="flex justify-between items-center">
                        <div className="space-y-1">
                          <div className="text-zinc-700">
                            {index === 1 ? (
                              <>
                                <p className="font-semibold">
                                  <span className="text-[#1429A0]">{guideData[index].text[0]} </span>
                                  {guideData[index].text[1]}
                                </p>
                              </>
                            ) : (
                              <>
                                <p className="text-[#1429A0] font-semibold">{guideData[index].text[0]}</p>
                                <p className="font-semibold">{guideData[index].text[1]}</p>
                              </>
                            )}
                          </div>
                          <p className="text-sm text-zinc-600">
                            {index === 1 ? (
                              <>
                                {guideData[index].text[2]}
                                <br />
                                {guideData[index].text[3]}
                              </>
                            ) : (
                              guideData[index].text[2]
                            )}
                          </p>
                        </div>
                        <img className="w-16 h-16" src={`/assets/${guideData[index].img}.png`} alt="유저" />
                      </div>
                    </div>

                    {type === "AUTO" ? (
                      <div className="text-right text-lg tracking-wide text-zinc-600 space-y-1">
                        <div className="flex justify-between">
                          <div className="flex items-center space-x-2">
                            <RiExchangeDollarLine className="text-2xl text-[#27995a]" />
                            <p>설정 희망환율</p>
                          </div>
                          <p className="font-semibold text-zinc-700">{autoExchangeInfo?.rate}원</p>
                        </div>
                        <div className="flex justify-between">
                          <div className="flex items-center space-x-3">
                            <FaCoins className="text-xl text-[#ffd753]" />
                            <p>자동환전 금액</p>
                          </div>
                          <p className="font-semibold text-zinc-700">
                            {formatCurrency(Number(autoExchangeInfo?.amount))}원
                          </p>
                        </div>
                      </div>
                    ) : (
                      <></>
                    )}
                  </>
                ) : (
                  <></>
                )
              )}
            </div>
          </div>
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        수정하기
      </button>
    </div>
  );
};
export default CurrentAutoExchangeInfo;
