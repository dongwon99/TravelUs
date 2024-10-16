import React from "react";
import { IoMdClose } from "react-icons/io";

interface PwaPromptProps {
  showPwaPrompt: boolean;
  hideForToday: boolean;
  setHideForToday: (hideForToday: boolean) => void;
  handlePwaDismiss: () => void;
}

const PwaPrompt: React.FC<PwaPromptProps> = ({ showPwaPrompt, hideForToday, setHideForToday, handlePwaDismiss }) => {
  // Component logic here

  return (
    <div>
      {showPwaPrompt && (
        <div className="pb-10 fixed inset-0 bg-gray-600 bg-opacity-50 flex items-end justify-center">
          <div className="w-[90%] bg-white p-6 rounded-2xl space-y-4">
            <div className="space-y-5">
              <div className="grid grid-cols-3">
                <div></div>
                <div className="mt-5 flex justify-center">
                  <img className="w-16 border rounded-2xl" src="/icons/192.png" alt="로고" />
                </div>
                <div className="flex justify-end">
                  <IoMdClose onClick={handlePwaDismiss} className="text-3xl" />
                </div>
              </div>
              <div className="pb-2 text-lg flex flex-col justify-center items-center">
                <p>
                  홈 화면에
                  <span className="font-bold"> TravelUs 앱</span>을 추가해야
                </p>
                <p>원활한 이용이 가능해요</p>
              </div>

              <hr />

              <div className="space-y-4">
                <p className="font-semibold text-lg">설치 방법</p>
                <div className="space-y-2">
                  <p className="w-24 px-1 py-[0.1rem] border-2 border-[#79a8ff] text-sm text-center text-zinc-700 rounded-3xl font-semibold">
                    안드로이드
                  </p>
                  <div className="ml-1 flex items-center space-x-1">
                    <p>화면 우측 상단</p>
                    <img className="w-7" src="/assets/pwa/3dotsButtons.png" alt="3dots" />
                    <p>
                      클릭 →<span className="text-[#1429A0] font-semibold"> 홈 화면에 추가</span>
                    </p>
                  </div>
                </div>

                <div className="space-y-2">
                  <p className="w-24 px-1 py-[0.1rem] border-2 border-[#79a8ff] text-sm text-center text-zinc-700 rounded-3xl font-semibold">
                    IOS
                  </p>
                  <div className="ml-1 flex items-center space-x-1">
                    <p>화면 하단 중앙</p>
                    <img className="w-7 rounded-lg" src="/assets/pwa/iosPwaIcon.png" alt="3dots" />
                    <p>
                      클릭 →<span className="text-[#1429A0] font-semibold"> 홈 화면에 추가</span>
                    </p>
                  </div>
                </div>
              </div>

              <div className="pt-3 flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="dontShowToday"
                  checked={hideForToday}
                  onChange={(e) => setHideForToday(e.target.checked)}
                />
                <label className="text-zinc-400 text-sm" htmlFor="dontShowToday">
                  오늘 하루 열지 않기
                </label>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PwaPrompt;
