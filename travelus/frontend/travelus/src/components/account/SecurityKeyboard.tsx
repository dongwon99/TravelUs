import React, { useState, MouseEvent, useCallback, useEffect } from "react";
import { LuDelete } from "react-icons/lu";
import { IoCloseOutline } from "react-icons/io5";
import { useDispatch } from "react-redux";
import { setIsKeyboard, setAccountPassword } from "../../redux/accountSlice";

const PASSWORD_MAX_LENGTH = 4; // 비밀번호 입력길이 제한 설정

const shuffle = (nums: number[]) => {
  let num_length = nums.length;
  while (num_length) {
    let random_index = Math.floor(num_length-- * Math.random());
    let temp = nums[random_index];
    nums[random_index] = nums[num_length];
    nums[num_length] = temp;
  }
  return nums;
};

const SecurityKeyboard = () => {
  const nums_init = Array.from({ length: 10 }, (v, k) => k);
  const [nums, setNums] = useState(nums_init);
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();

  const handlePasswordChange = useCallback(
    (num: number) => {
      let nums_random = Array.from({ length: 10 }, (v, k) => k);
      setNums(shuffle(nums_random));
      setPassword((prevPassword) => {
        if (prevPassword.length === PASSWORD_MAX_LENGTH) {
          return prevPassword;
        }
        const newPassword = prevPassword + num.toString();
        dispatch(setAccountPassword(newPassword));
        return newPassword;
      });
    },
    [dispatch]
  );

  const erasePasswordOne = useCallback(
    (e: MouseEvent) => {
      setPassword((prevPassword) => {
        const newPassword = prevPassword.slice(0, -1);
        dispatch(setAccountPassword(newPassword));
        return newPassword;
      });
    },
    [dispatch]
  );

  const shuffleNums = useCallback(
    (num: number) => (e: MouseEvent) => {
      let nums_random = Array.from({ length: 10 }, (v, k) => k);
      setNums(shuffle(nums_random));
      handlePasswordChange(num);
    },
    [handlePasswordChange]
  );

  const handleKeyboardClose = () => {
    dispatch(setIsKeyboard(false));
  };

  // 비밀번호 길이가 PASSWORD_MAX_LENGTH에 도달하면 handleKeyboardClose 호출
  useEffect(() => {
    if (password.length === PASSWORD_MAX_LENGTH) {
      handleKeyboardClose();
    }
  }, [password]);

  // 키보드가 열릴 때마다 배열을 무작위로 설정
  useEffect(() => {
    setNums(shuffle(Array.from({ length: 10 }, (v, k) => k)));
  }, []);

  return (
    <div className="bg-[#EFEFF5] rounded-3xl relative flex flex-col">
      <IoCloseOutline className="text-3xl absolute top-5 left-5" onClick={handleKeyboardClose} />
      <div className="py-14 flex flex-col space-y-3">
        <p className="text-lg text-center font-semibold">비밀번호 입력</p>
      </div>
      <div className="p-5 bg-white grid grid-cols-3 gap-5">
        {nums.map((n, i) => {
          const Basic_button = (
            <button className="text-2xl font-semibold" value={n} onClick={() => handlePasswordChange(n)} key={i}>
              {n}
            </button>
          );
          return i === nums.length - 1 ? (
            <>
              <button className=""></button>
              {Basic_button}
            </>
          ) : (
            Basic_button
          );
        })}
        <button className="text-2xl flex justify-center" onClick={erasePasswordOne}>
          <LuDelete />
        </button>
      </div>
    </div>
  );
};

export default SecurityKeyboard;
