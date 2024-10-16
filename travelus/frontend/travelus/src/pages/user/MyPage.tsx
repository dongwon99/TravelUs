import React, { useRef } from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { useDispatch } from "react-redux";
import { editUserInformation } from "../../redux/userInformationSlice";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../api/user";
import { IoCamera } from "react-icons/io5";
import { UserInfo } from "../../types/userInformation";
import api from "../../lib/axios";
import { notificationApi } from "../../api/notification";
import Loading from "../../components/loading/Loading";

const MyPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [userData, setUserData] = useState<UserInfo | null>(null);
  const [profileImage, setProfileImage] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const doLogout = async () => {
    const userId = localStorage.getItem("userId");

    if (!userId) {
      console.error("유저 ID가 없습니다.");
      return;
    }

    try {
      // userId를 숫자 타입으로 변환
      const userIdNumber = parseInt(userId, 10);

      // FCM 토큰 삭제 API 호출
      await notificationApi.deleteFcmToken(userIdNumber);
      console.log("FCM 토큰이 성공적으로 삭제되었습니다.");
    } catch (error) {
      console.error("FCM 토큰 삭제 중 오류 발생:", error);
    }

    // 로컬 스토리지 및 세션 스토리지 지우기
    localStorage.clear();
    sessionStorage.clear();

    // 로그인 페이지로 리다이렉트
    navigate("/login");
  };

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      userApi
        .fetchUser()
        .then((response) => {
          setProfileImage(response.data.profileImg); // 프로필 이미지 설정
          setUserData(response.data); // API 응답 데이터를 상태로 설정
          dispatch(editUserInformation(response.data)); // Redux 스토어에 유저 정보 저장
        })
        .catch((error) => {
          console.error("Failed to fetch user data: ", error);
        });
    }
  }, []);

  const handleImageChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setProfileImage(reader.result as string); // 미리보기 이미지 설정
      };
      reader.readAsDataURL(file);

      // 프로필 이미지 업로드 API 호출
      const formData = new FormData();
      formData.append("profileImg", file);
      try {
        const response = await userApi.uploadProfileImage(formData);
        if (response.status === 200) {
          console.log("프로필 이미지 업로드 성공");
        }
      } catch (error) {
        console.error("프로필 이미지 업로드 실패: ", error);
      }
    }
  };

  // 프로필 이미지를 클릭하면 파일 입력창 열기
  const handleImageClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const formatPhoneNumber = (phoneNumber: string) => {
    return phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
  };

  return (
    <div className="w-full h-full pb-16 flex flex-col">
      <div className="bg-[#F3F4F6] p-5 space-y-7">
        <div className="flex space-x-1 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate("/");
            }}
            className="text-2xl"
          />
          <p className="text-lg font-bold">내 정보</p>
        </div>
        <div className="flex flex-col justify-center items-center space-y-3">
          <div className="relative w-20 h-20">
            <img
              className={profileImage ? `w-full h-full rounded-full border object-cover` : `w-full h-full rounded-full`}
              onClick={handleImageClick}
              src={profileImage ? profileImage : "/assets/loadingIcon.png"}
              alt="유저아이콘"
            />
            <div className="w-[1.8rem] h-[1.8rem] rounded-full bg-white border border-zinc-500 absolute -right-[0.3rem] bottom-0 flex justify-center items-center">
              <IoCamera className="text-lg" />
            </div>
          </div>
          <input
            type="file"
            accept="image/*"
            ref={fileInputRef}
            onChange={handleImageChange}
            className="hidden" // input 요소를 숨김
          />
          <p className="font-semibold">{userData ? userData.name : "Loading..."}</p>
        </div>
      </div>
      <hr className="border-2 border-[#F3F4F6]" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">기본 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p>이름</p>
            <p>{userData ? userData.name : "Loading..."}</p>
          </div>
          <div className="flex justify-between">
            <p>생년월일</p>
            <p>{userData ? userData.birth : "Loading..."}</p>
          </div>
          <div className="flex justify-between">
            <p>휴대폰번호</p>
            <p>{userData ? formatPhoneNumber(userData.phone) : "Loading..."}</p>
          </div>
        </div>
      </div>
      <hr className="border-2 border-[#F3F4F6]" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">집 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p className="w-32">주소</p>
            <p>{userData ? userData.address : "Loading..."}</p>
          </div>
        </div>
      </div>
      <hr className="border-2 border-[#F3F4F6]" />
      <div className="px-5 py-4 flex justify-center">
        <button
          onClick={() => {
            navigate("/userupdate");
          }}
          className="w-full p-2 border rounded-md border-zinc-300">
          정보 수정
        </button>
      </div>
      <div className="px-5 flex justify-center">
        <button onClick={doLogout} className="w-full p-2 border rounded-md border-zinc-300">
          로그아웃
        </button>
      </div>
    </div>
  );
};

export default MyPage;
