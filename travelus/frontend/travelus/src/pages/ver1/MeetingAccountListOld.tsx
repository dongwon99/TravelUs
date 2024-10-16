import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { editJoinedAccountList } from "../../redux/accountSlice";
import MeetingAccount from "../../components/account/MeetingAccountOld";
import JoinedMeetingAccount from "../../components/account/JoinedMeetingAccountOld";
import { useEffect } from "react";
import { accountApi } from "../../api/account";

const MeetingAccountList = () => {
  const accountList = useSelector((state: RootState) => state.account.accountList);
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);
  const joinedAccountList = useSelector((state: RootState) => state.account.joinedAccountList);

  // 배열의 길이가 0이면 0을, 1 이상이면 accountList.length - 1을 표시
  const accountCount = accountList.length === 0 ? 0 : accountList.length - 1;
  const joinedAccountCount = joinedAccountList.length;

  const dispatch = useDispatch();
  const userId = localStorage.getItem("userId") || "";
  const userIdNumber = userId ? parseInt(userId, 10) : 0;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await accountApi.fetchJoinedMeetingAccount();
        // Redux 스토어에 데이터 저장
        dispatch(editJoinedAccountList(response));
      } catch (error) {
        console.error("Error fetching data:", error);
        alert("내가 가입한 모임 통장 조회에 실패했습니다.");
      }
    };

    fetchData();
  }, [dispatch]); // 의존성 배열에 필요한 값 추가

  return (
    <div className="w-full">
      <div className="w-full p-5 flex flex-col items-center space-y-8">
        <div className="w-full flex justify-start">
          <p className="text-xl font-bold">내 모임통장 계좌</p>
        </div>

        <div className="w-full flex flex-col space-y-3">
          {/* 모임 통장 총 개수 표시 */}
          <div className="w-full mb-2 py-3 px-5 flex flex-col rounded-xl bg-[#e6e6e6]">
            <p className="font-bold">
              총 <span className="text-blue-500">{accountCount + joinedAccountCount}</span>개
            </p>
          </div>

          {/* 내가 개설한 모임 통장 있을 시 표시 */}
          {accountList.length > 1 ? (
            <div>
              <p className="mb-3 font-bold">내가 개설한 모임통장</p>
              <div className="flex flex-col space-y-3">
                {accountList.slice(1).map((account, index) => (
                  <MeetingAccount
                    key={index}
                    index={index}
                    account={account}
                    foreignAccount={foreignAccountList[index]}
                  />
                ))}
              </div>
            </div>
          ) : (
            <div>
              <p className="mb-3 font-bold">내가 개설한 모임통장</p>
              <p className="text-sm">개설된 모임 통장이 없어요</p>
            </div>
          )}

          {/* 내가 가입한 모임 통장 있을 시 표시 */}
          {joinedAccountList.length > 0 ? (
            <div>
              <p className="my-3 font-bold">참여중인 모임통장</p>
              <div className="flex flex-col space-y-3">
                {/* {joinedAccountList.map((account, index) => (
                  <JoinedMeetingAccount key={index} accountId={account.groupId} index={index} account={account} />
                ))} */}
              </div>
            </div>
          ) : (
            <div>
              <p className="my-3 font-bold">참여중인 모임통장</p>
              <p className="text-sm">참여 중인 모임 통장이 없어요</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MeetingAccountList;
