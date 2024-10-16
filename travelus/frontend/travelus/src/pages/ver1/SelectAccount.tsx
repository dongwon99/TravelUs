import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { IoIosArrowDown } from "react-icons/io";
import { accountApi } from "../../api/account";
import { AccountInfo } from "../../types/account";
import Loading from "../../components/loading/Loading";

// 에시 데이터들, axios로 백에서 데이터 받아와야 함
const SelectAccount = (): React.ReactElement => {
  const [generalAccounts, setGeneralAccounts] = useState<AccountInfo[]>([]);
  const [foreignAccounts, setForeignAccounts] = useState<AccountInfo[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<AccountInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  // const { userId } = useParams<{ userId: string }>();

  const userId = 2;

  const navigate = useNavigate();

  useEffect(() => {
    const loadAccountData = async () => {
      if (userId) {
        try {
          setIsLoading(true);
          const [generalAccount, foreignAccount] = await Promise.all([
            accountApi.fetchAccountInfo(userId),
            accountApi.fetchForeignAccountInfo(userId),
          ]);
          setGeneralAccounts(generalAccount);
          setForeignAccounts(foreignAccount);
          if (generalAccount.length > 0) {
            setSelectedAccount(generalAccount[0]);
          }
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
    setSelectedAccount(account);
    setIsDropdownOpen(false);
  };

  const handleExchangeButton = () => {
    if (selectedAccount && foreignAccounts.length > 0) {
      navigate(`/exchange`);
    }
  };

  if (isLoading) return <Loading />;
  if (error) return <div>{error}</div>;
  if (generalAccounts.length === 0 && foreignAccounts.length === 0) return <div>계좌 정보를 찾을 수 없습니다.</div>;

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
      <h1 className="text-xl font-bold mb-4">모임 통장 선택</h1>
      <div className="mb-4 relative">
        <button
          className="w-full flex items-center justify-between bg-white rounded-lg p-3 shadow"
          onClick={() => setIsDropdownOpen(!isDropdownOpen)}>
          <span>{selectedAccount ? selectedAccount.accountName : "환전할 모임 통장 선택"}</span>
          <IoIosArrowDown className="w-5 h-5" />
        </button>
        {isDropdownOpen && (
          <div className="absolute w-full mt-1 bg-white rounded-lg shadow-lg z-10">
            {generalAccounts.map((account) => (
              <button
                key={account.accountNo}
                className="w-full text-left p-3 hover:bg-gray-100"
                onClick={() => handleAccountSelect(account)}>
                {account.accountName} - {account.accountNo}
              </button>
            ))}
          </div>
        )}
      </div>
      {selectedAccount && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg p-4 shadow">
            <h2 className="font-bold mb-2">일반모임통장</h2>
            <div className="flex items-center mb-2">
              <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
              <div>
                <p className="font-semibold">{selectedAccount.accountName}</p>
                <p className="text-sm text-gray-500">{selectedAccount.accountNo}</p>
              </div>
            </div>
            {/* <p className="text-right font-bold">{selectedAccount.accountBalance.toLocaleString()} KRW</p> */}
          </div>

          <div className="flex justify-center items-center my-4">
            <IoIosArrowDown className="w-8 h-8 text-[#0046FF]" />
          </div>

          {foreignAccounts.length > 0 && (
            <div className="bg-white rounded-lg p-4 shadow">
              <h2 className="font-bold mb-2">외화모임통장</h2>
              <div className="flex items-center mb-2">
                <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
                <div>
                  <p className="font-semibold">{foreignAccounts[0].accountName}</p>
                  <p className="text-sm text-gray-500">{foreignAccounts[0].accountNo}</p>
                </div>
              </div>
              {/* <p className="text-right font-bold">{foreignAccounts[0].accountBalance.toLocaleString()} {foreignAccounts[0].currency}</p> */}
            </div>
          )}
        </div>
      )}
      <button
        className="w-full bg-[#0046FF] text-white py-3 rounded-lg mt-4"
        onClick={handleExchangeButton}
        disabled={!selectedAccount || foreignAccounts.length === 0}>
        환전하기
      </button>
    </div>
  );
};

export default SelectAccount;
