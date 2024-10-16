import { useNavigate } from "react-router";
import { useEffect, useState } from "react";
import UpdateUserForm from "../../components/user/UpdateUserForm";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../api/user";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { editUserInformation } from "../../redux/userInformationSlice";

const UserUpdate = () => {
  const navigate = useNavigate();
  const [inputs, setInputs] = useState({
    id: useSelector((state: RootState) => state.userInformation.UserInfo.id),
    name: useSelector((state: RootState) => state.userInformation.UserInfo.name),
    birth: useSelector((state: RootState) => state.userInformation.UserInfo.birth),
    phone: useSelector((state: RootState) => state.userInformation.UserInfo.phone),
    address: useSelector((state: RootState) => state.userInformation.UserInfo.address),
  });

  const formatBirthday = (birthday: string): string => {
    if (birthday.length === 10) {
      return birthday;
    }

    return `${birthday.slice(0, 4)}-${birthday.slice(4, 6)}-${birthday.slice(6, 8)}`;
  };

  const handleUpdate = async () => {
    const formData = new FormData();

    formData.append("name", inputs.name);
    formData.append("phone", inputs.phone);
    formData.append("address", inputs.address);
    formData.append("birth", formatBirthday(inputs.birth));

    try {
      const response = await userApi.editUserInformation(formData);
      if (response.status === 200) {
        navigate("/mypage");
      }
    } catch (error) {
      console.error("유저 정보 수정 에러", error);
    }
  };

  return (
    <div className="w-full h-full p-5 pb-8 bg-white flex flex-col justify-between">
      <div className="flex flex-col space-y-10">
        <div className="flex items-center space-x-1">
          <IoIosArrowBack
            onClick={() => {
              navigate("/mypage");
            }}
            className="text-2xl"
          />
          <p className="text-lg font-bold">내 정보 수정</p>
        </div>
        <UpdateUserForm inputs={inputs} setInputs={setInputs} />
      </div>
      <div className="flex flex-col"></div>
      <button
        onClick={() => {
          handleUpdate();
        }}
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white font-semibold bg-[#1429A0]">
        수정하기
      </button>
    </div>
  );
};

export default UserUpdate;
