import React, { useState, MouseEvent, useCallback, useEffect } from "react";
import { FaArrowLeft } from "react-icons/fa";

interface Props {
  password: string;
  setPassword: (password: string | ((prevPassword: string) => string)) => void;
}

const SecurityNumberKeyboard = ({ password, setPassword }: Props) => {
  const PASSWORD_MAX_LENGTH = 4; // 비밀번호 입력 길이 제한 설정
  const nums_init = Array.from({ length: 10 }, (v, k) => k);
  const [nums, setNums] = useState(nums_init); // 숫자 배열 초기화
  const [activeButton, setActiveButton] = useState<Set<number> | null>(null); // 숫자 배열 초기화

  // 키보드가 처음 열릴 때만 배열을 무작위로 섞음
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

  // 숫자 배열을 섞지 않고 비밀번호 입력 처리
  const handlePasswordChange = useCallback((num: number) => {
    setPassword((prev) => {
      if (prev.length === PASSWORD_MAX_LENGTH) {
        return prev;
      }
      return prev + num.toString();
    });
  }, []);

  // 비밀번호 한 자리 삭제
  const erasePasswordOne = useCallback((e: MouseEvent) => {
    setPassword((prev) => prev.slice(0, -1));
  }, []);

  const handleTouchEnd = () => {
    setActiveButton(null);
  };

  const handleTouchStart = (n: number) => {
    const tempSet = new Set([n]);
    while (tempSet.size < 3) {
      const num = Math.floor(Math.random() * 10);
      tempSet.add(num);
    }
    setActiveButton(tempSet);
  };

  useEffect(() => {
    setNums(shuffle(Array.from({ length: 10 }, (v, k) => k)));
  }, []);

  return (
    <div>
      <div className="p-5 bg-white grid grid-cols-3 gap-3">
        {nums.map((n, i) => {
          const Basic_button = (
            <button
              className={`py-3 text-3xl font-medium ${activeButton?.has(n) ? "bg-[#F2F3F5] rounded-xl" : ""}`}
              value={n}
              onClick={() => handlePasswordChange(n)}
              onTouchStart={() => handleTouchStart(n)}
              onTouchEnd={() => handleTouchEnd()}
              key={i}>
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
          <FaArrowLeft />
        </button>
      </div>
    </div>
  );
};

export default SecurityNumberKeyboard;
