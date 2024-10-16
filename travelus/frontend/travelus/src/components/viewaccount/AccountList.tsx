import React from 'react';
import { AccountInfo } from "../../types/account";

type AccountDetailsProps = {
  accounts: AccountInfo[];
  onSelectAccount: (account: AccountInfo) => void;
};

const AccountList = ({ accounts, onSelectAccount }: AccountDetailsProps): React.ReactElement => {
  return (
    <div>
      {accounts.map((account) => (
        <div 
          key={account.accountNo} 
          className='p-4 mb-4 bg-white rounded-lg shadow cursor-pointer hover:bg-gray-50'
          onClick={() => onSelectAccount(account)}
        >
          <div className='mb-2 flex items-center'>
            <div className='w-8 h-8 mr-2 bg-[#0046FF] rounded-full'></div>
            <div className='ml-3'>
              <p className='font-semibold'>{account.accountName}</p>
              <p className='text-sm text-gray-500'>{account.accountNo}</p>
            </div>
          </div>
          <p className='text-2xl font-bold text-right'>
            {/* {Number(account.accountBalance).toLocaleString()} {account.currency} */}
          </p>
        </div>
      ))}
    </div>
  );
};

export default AccountList;