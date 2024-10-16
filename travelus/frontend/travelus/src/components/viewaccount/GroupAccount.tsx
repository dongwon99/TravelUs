import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { accountApi } from "../../api/account";
import { AccountInfo } from "../../types/account";
import AccountList from "./AccountList";
import Loading from "../loading/Loading";

const GroupAccount = (): React.ReactElement => {
  const [generalAccounts, setGeneralAccounts] = useState<AccountInfo[]>([]);
  const [foreignAccounts, setForeignAccounts] = useState<AccountInfo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // const { userId } = useParams<{ userId: string }>();

  const userId = 2;
  const navigate = useNavigate();

  useEffect(() => {
    const loadAccountData = async () => {
      if (userId) {
        try {
          setIsLoading(true);
          // const [generalAccount, foreignAccount] = await Promise.all([
          //   accountApi.fetchAccountInfo(userId),
          //   accountApi.fetchForeignAccountInfo(userId)
          // ]);
          // console.log(generalAccount, foreignAccount)
          // setGeneralAccounts(generalAccount);
          // setForeignAccounts(foreignAccount);
        } catch (error) {
          setError("계좌 정보를 불러오는 데 실패했습니다.");
          console.error("Error fetching account data:", error);
        } finally {
          setIsLoading(false);
        }
      }
    };
    loadAccountData();
  }, [userId]);

  const handleAccountSelect = (account: AccountInfo) => {
    // 계좌 선택 시 해당 계좌의 상세 페이지로 이동
    navigate(`/account/${account.accountNo}`);
  };

  if (isLoading) return <Loading />;
  if (error) return <div>{error}</div>;
  if (generalAccounts.length === 0 && foreignAccounts.length === 0) return <div>계좌 정보를 찾을 수 없습니다.</div>;

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
      <h1 className="text-2xl font-bold mb-4">내 계좌 목록</h1>

      {generalAccounts.length > 0 && (
        <>
          <h2 className="text-xl font-semibold mb-2">개인계좌 & 일반모임통장</h2>
          <AccountList key="general" accounts={generalAccounts} onSelectAccount={handleAccountSelect} />
        </>
      )}

      {foreignAccounts.length > 0 && (
        <>
          <h2 className="text-xl font-semibold mb-2 mt-4">외화모임통장</h2>
          <AccountList key="foreign" accounts={foreignAccounts} onSelectAccount={handleAccountSelect} />
        </>
      )}
    </div>
  );
};

export default GroupAccount;
