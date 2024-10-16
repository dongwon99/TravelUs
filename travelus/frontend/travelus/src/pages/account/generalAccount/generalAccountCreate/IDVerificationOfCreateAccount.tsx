import React, { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { useDispatch } from "react-redux";
import { accountApi } from "../../../../api/account";
import { userApi } from "../../../../api/user";
import { UserInfo } from "../../../../types/userInformation";
import Lottie from "lottie-react";
import idcardLoadingAnimation from "../../../../lottie/idcardLoadingAnimation.json";
import Loading from "../../../../components/loading/Loading";
import { IoIosArrowBack } from "react-icons/io";

const IDVerificationOfCreateAccount = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const password = location.state.password;
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [loadingTextIdx, setLoadingTextIdx] = useState(0);

  const guideText = [
    "신분증 앞면이 선명하게 보이도록 가로로 촬영해주세요.",
    "검정색 등 어두운 바탕 위에서 촬영해주세요.",
    "빛이 반사되지 않는 곳에서 촬영해주세요.",
    "훼손되지 않은 신분증을 촬영해주세요.",
  ];

  const loadingText = [
    ["신분증이 잘 찍혔는지", "확인하고 있어요"],
    [`${userInfo?.name}님의 정보가 맞는지`, "확인하고 있어요"],
    ["거의 다 확인했어요", "조금만 더 기다려주세요"],
  ];

  // File 파싱
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      fetchIdcard(file);
    }
  };

  // 사진 업로드창 열기
  const handleUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 생일 포맷팅
  const formatBirth = (birth: string) => {
    const temp = birth.replace(/-/g, "");
    return temp.slice(2);
  };

  // 신분증 정보 조회
  const fetchIdcard = async (file: File) => {
    setIsLoading(true);
    const formData = new FormData();
    formData.append("file", file);
    try {
      const response = await userApi.fetchIdcard(formData);
      if (response.status === 200) {
        if (checkInfo(response.data.name, response.data.residentRegistrationNumber)) {
          navigate("/account/create/request", { state: { password: location.state.password } });
        } else {
          alert("신분증 본인인증에 실패했습니다. 다시 촬영해주세요.");
        }
      }
    } catch (error) {
      console.log("userApi의 fetchIdcard : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 신분증 정보와 비교할 사용자 정보 조회
  const getUserInfo = async () => {
    try {
      const response = await userApi.fetchUser();
      setUserInfo(response.data);
    } catch (error) {
      console.log("userApi의 fetchUser : ", error);
    }
  };

  const checkInfo = (idcardName: string, idcardNumber: string) => {
    if (userInfo) {
      const idcardBirth = idcardNumber.slice(0, 6);
      const idcardGender = Number(idcardNumber.slice(7, 8)) % 2 === 1 ? "MALE" : "FEMALE";
      const formatUserBirth = formatBirth(userInfo?.birth);
      if (idcardName === userInfo.name && idcardBirth === formatUserBirth && idcardGender === userInfo.gender) {
        return true;
      }
      return false;
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setLoadingTextIdx((prev) => (prev + 1) % 3);
    }, 3000);

    return () => clearInterval(intervalId); // 컴포넌트가 언마운트될 때 interval 정리
  }, [isLoading]);

  if (!userInfo) {
    return <Loading />;
  }

  if (isLoading) {
    return (
      <div className="h-full p-5 pb-8">
        <div className="grid gap-14">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>

          <div className="grid gap-20">
            <p className="text-2xl font-semibold">
              {loadingText[loadingTextIdx][0]}
              <br />
              {loadingText[loadingTextIdx][1]}
            </p>

            <div className="flex justify-center">
              <Lottie className="w-1/2" animationData={idcardLoadingAnimation} />
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="mt-20 flex flex-col justify-center space-y-10">
        <div className="grid gap-3">
          <p className="text-2xl font-semibold">
            주민등록증이나
            <br />
            운전면허증을 준비해주세요
          </p>
          <p className="text-[#565656]">안전한 거래를 위해 본인 인증이 필요해요.</p>
        </div>

        <img className="p-5" src="/assets/IDVerification.png" alt="" />

        <div className="px-5 grid gap-3">
          {guideText.map((text, index) => (
            <div className="flex items-center space-x-2" key={index}>
              <div className="w-5 aspect-1 bg-[#1429A0] rounded-full flex justify-center items-center">
                <p className="text-xs text-white ">{index + 1}</p>
              </div>
              <p className="text-xs text-[#565656]">{text}</p>
            </div>
          ))}
        </div>
      </div>
      {/* 숨겨진 파일 입력 요소 */}
      <input className="hidden" type="file" ref={fileInputRef} onChange={handleFileChange} accept="image/*" />

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleUploadClick()}>
        신분증 인증하기
      </button>
    </div>
  );
};

export default IDVerificationOfCreateAccount;
