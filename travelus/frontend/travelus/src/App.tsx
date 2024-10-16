import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "../src/pages/MainPage";
import Login from "./pages/user/Login";
import SignUpBasicInformation from "./pages/user/SignUpBasicInformation";
import SignUpAddress from "./pages/user/SignUpAddress";
import MyPage from "./pages/user/MyPage";
import UserUpdate from "./pages/user/UserUpdate";
import UserPhoneUpdate from "./pages/user/UserPhoneUpdate";
import UserAddressUpdate from "./pages/user/UserAddressUpdate";
import UserPasswordUpdate from "./pages/user/UserPasswordUpdate";
import MeetingAccountList from "./pages/account/meetingAccount/MeetingAccountList";
import MeetingAccountDetail from "./pages/account/meetingAccount/MeetingAccountDetail";
import MeetingAccountManagement from "./pages/account/meetingAccount/MeetingAccountManagement";
import MeetingAccountGroupMember from "./pages/account/meetingAccount/MeetingAccountGroupMember";
import MeetingAccountUpdate from "./pages/account/meetingAccount/meetingAccountUpdate/MeetingAccountUpdate";
import JoinedMeetingAccountDetail from "./pages/account/meetingAccount/JoinedMeetingAccountDetail";
import AccountTransaction from "./pages/account/generalAccount/AccountTransaction";
import TransferSelectBank from "./pages/transfer/TransferSelectBank";
import TransferSetMoney from "./pages/transfer/TransferSetMoney";
import TransferPassword from "./pages/transfer/TransferPassword";
import TransferConfirm from "./pages/transfer/TransferConfirm";
import TransferSuccess from "./pages/transfer/TransferSuccess";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import AccountCreate from "./pages/ver1/AccountCreateOld";
import ExchangeRate from "./pages/exchange/ExchangeRate";
import ExchangeDetail from "./components/exchange/ExchangeDetail";
import Exchange from "./pages/exchange/Exchange";
import ExchangeKRWFlow from "./pages/exchange/ExchangeKRW";
import ExchangeCompletion from "./pages/exchange/ExchangeCompletion";
import ExchangeRateForecastDetail from "./pages/exchange/ExchangeRateForecastDetail";
import ExchangeRateForecast from "./pages/exchange/ExchangeRateForecast";
import AccountPasswordInput from "./pages/exchange/ExchangeConfirmation";
import SelectAccount from "./pages/ver1/SelectAccount";
import AccountCreateComplete from "./pages/ver1/AccountCreateComplete";
import GeneralMeetingAccountCreate from "./pages/ver1/GeneralMeetingAccountCreate";
import MeetingAccountCreatePrepare from "./pages/meetingAccount/createMeetingAccount/PrepareOfCreateMeetingAccount";
import MeetingAccountCreateComplete from "./pages/ver1/MeetingAccountCreateComplete";
import AccountBookDetail from "./pages/accountBook/AccountBookDetail";
import PrivateRoute from "./pages/user/PrivateRoute";
import EditMembers from "./pages/settlement/EditMembers";
import UserInfoOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/UserInfoOfCreateAccount";
import PasswordOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/PasswordOfCreateAccount";
import CheckPasswordOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/CheckPasswordOfCreateAccount";
import IDVerificationOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/IDVerificationOfCreateAccount";
import UserInfoOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/UserInfoOfCreateMeetingAccount";
import CompletedOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/CompletedOfCreateAccount";
import MeetingInfoOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/MeetingInfoOfCreateMeetingAccount";
import PasswordOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/PasswordOfCreateMeetingAccount";
import CheckPasswordOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/CheckPasswordOfCreateMeetingAccount";
import CompletedOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/CompletedOfCreateMeetingAccount";
import IDVerificationOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/IDVerificationOfCreateMeetingAccount";
import TravelBoxTransaction from "./pages/travelBox/TravelBoxTransaction";
import MeetingTransaction from "./pages/meetingAccount/MeetingTransaction";
import CurrencyInfoOfCreateTravelBox from "./pages/travelBox/CurrencyInfoOfCreateTravelBox";
import AutoCurrencyExchangeRateOfCreateTravelBox from "./pages/travelBox/AutoCurrencyExchangeRateOfCreateTravelBox";
import DeleteTravelBox from "./pages/travelBox/travelBoxDelete/DeleteTravelbox";
import DeleteTravelBoxPassword from "./pages/travelBox/travelBoxDelete/DeleteTravelboxPassword";
import CompletedOfDeleteTravelBox from "./pages/travelBox/travelBoxDelete/CompletedOfDeleteTravelBox";
import SelectSettlementAmount from "./pages/settlement/balanceSettlement/SelectSettlementAmount";
import BalanceSettlementCompleted from "./pages/settlement/balanceSettlement/BalanceSettlementCompleted";
import SettlementInfo from "./pages/settlement/balanceSettlement/SettlementInfo";
import ExpenditureTransactionDetail from "./pages/settlement/expenditureSettlement/ExpenditureTransactionDetail";
import ExpenditureSettlementInfo from "./pages/settlement/expenditureSettlement/ExpenditureSettlementInfo";
import ExpenditureSettlementCompleted from "./pages/settlement/expenditureSettlement/ExpenditureSettlementCompleted";
import SelectTypeOfAutoExchange from "./pages/travelBox/SelectTypeOfAutoExchange";
import TravelBoxCreatePrepare from "./pages/travelBox/TravelBoxCreatePrepare";
import InvitationOfMeeting from "./pages/meetingAccount/inviteMeeting/InvitationOfMeeting";
import InviteInfoOfMeeting from "./pages/meetingAccount/inviteMeeting/InviteInfoOfMeeting";
import AlreadyInviteOfMeeting from "./pages/meetingAccount/inviteMeeting/AlreadyInviteOfMeeting";
import FillSetMoney from "./pages/account/meetingAccount/meetingAccountFill/FillSetMoney";
import FillPassword from "./pages/account/meetingAccount/meetingAccountFill/FillPassword";
import FillConfirm from "./pages/account/meetingAccount/meetingAccountFill/FillConfirm";
import FillSuccess from "./pages/account/meetingAccount/meetingAccountFill/FillSuccess";
import SelectAccountOfMeeting from "./pages/meetingAccount/SelectAccountOfMeeting";
import EnglishNameOfCreateCard from "./pages/card/cardCreate/EnglishNameOfCreateCard";
import PasswordOfCreateCard from "./pages/card/cardCreate/PasswordOfCreateCard";
import CheckPasswordOfCreateCard from "./pages/card/cardCreate/CheckPasswordOfCreateCard";
import PasswordOfBalanceSettlement from "./pages/settlement/balanceSettlement/PasswordOfBalanceSettlement";
import AddressOfCreateCard from "./pages/card/cardCreate/AddressOfCreateCard";
import CompletedOfCreateCard from "./pages/card/cardCreate/CompletedOfCreateCard";
import CardDetail from "./pages/card/cardDetail/CardDetail";
import CardTransaction from "./pages/card/cardTransaction/cardTransaction";
import ExpenditureSettlementList from "./pages/settlement/expenditureSettlement/ExpenditureSettlementList";
import NotificationList from "./pages/notification/notificationList";
import SettlementTransferConfirm from "./pages/settlement/expenditureSettlement/settlementTransfer/SettlementTransferConfirm";
import SettlementTransferPassword from "./pages/settlement/expenditureSettlement/settlementTransfer/SettlementTransferPassword";
import SettlementTransferSetMoney from "./pages/settlement/expenditureSettlement/settlementTransfer/SettlementTransferSetMoney";
import ExpenditureSettlementDetail from "./pages/settlement/expenditureSettlement/ExpenditureSettlementDetail";
import SettlementTransferSuccess from "./pages/settlement/expenditureSettlement/settlementTransfer/SettlementTransferSuccess";
import CreateRequestOfCreateMeetingAccount from "./pages/meetingAccount/createMeetingAccount/CreateRequestOfCreateMeetingAccount";
import CreateRequestOfCreateAccount from "./pages/account/generalAccount/generalAccountCreate/CreateRequestOfCreateAccount";
import ExpenditureSettlementGroupList from "./pages/account/meetingAccount/expenditureSettlement/ExpenditureSettlementGroupList";
import ExpenditureSettlementGroupDetail from "./pages/account/meetingAccount/expenditureSettlement/ExpenditureSettlementGroupDetail";
import CreateAccountBookInfo from "./pages/accountBook/CreateAccountBookInfo";
import AutoCurrencyExchangeAmountOfCreateTravelBox from "./pages/travelBox/AutoCurrencyExchangeAmountOfCreateTravelBox";
import CompletedOfCreateAutoExchange from "./pages/travelBox/CompletedOfCreateAutoExchange";
import CurrentAutoExchangeInfo from "./pages/travelBox/CurrentAutoExchangeInfo";

function App() {
  return (
    <div className="h-full">
      <BrowserRouter>
        <Routes>
          <Route
            path="/*"
            element={
              <>
                {/* 페이지에 Header와 Footer가 모두 포함된 경로 */}
                <Header />
                <div style={{ paddingBottom: "64px", backgroundColor: "#F3F4F6", minHeight: "100vh" }}>
                  <Routes>
                    <Route element={<PrivateRoute />}>
                      {/* 메인페이지 */}
                      <Route path="/" element={<MainPage />} />

                      {/* 모임통장 목록 */}
                      <Route path="/meetingaccountlist" element={<MeetingAccountList />} />

                      {/* 머니로그 */}
                      <Route path="/accountbookdetail" element={<AccountBookDetail />} />

                      {/* 전체 환율 */}
                      <Route path="/exchangerate" element={<ExchangeRate />} />

                      {/* Add other protected routes here */}
                    </Route>
                  </Routes>
                </div>
                <Footer />
              </>
            }
          />

          {/* 회원 */}
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUpBasicInformation />} />
          <Route path="/signup/address" element={<SignUpAddress />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/userupdate" element={<UserUpdate />} />
          <Route path="/userupdate/phone" element={<UserPhoneUpdate />} />
          <Route path="/userupdate/address" element={<UserAddressUpdate />} />
          <Route path="/userupdate/password" element={<UserPasswordUpdate />} />

          {/* 입출금통장 생성 */}
          <Route path="/account/create/userinfo" element={<UserInfoOfCreateAccount />} />
          <Route path="/account/create/password" element={<PasswordOfCreateAccount />} />
          <Route path="/account/create/password/check" element={<CheckPasswordOfCreateAccount />} />
          <Route path="/account/create/idverification" element={<IDVerificationOfCreateAccount />} />
          <Route path="/account/create/request" element={<CreateRequestOfCreateAccount />} />
          <Route path="/account/create/completed" element={<CompletedOfCreateAccount />} />

          {/* 모임통장 생성 */}
          <Route path="/meeting/create/prepare" element={<MeetingAccountCreatePrepare />} />
          <Route path="/meeting/create/userinfo" element={<UserInfoOfCreateMeetingAccount />} />
          <Route path="/meeting/create/meetinginfo" element={<MeetingInfoOfCreateMeetingAccount />} />
          <Route path="/meeting/create/select/account" element={<SelectAccountOfMeeting />} />
          <Route path="/meeting/create/password/:type" element={<PasswordOfCreateMeetingAccount />} />
          <Route path="/meeting/create/password/check" element={<CheckPasswordOfCreateMeetingAccount />} />
          <Route path="/meeting/create/idverification" element={<IDVerificationOfCreateMeetingAccount />} />
          <Route path="/meeting/create/request" element={<CreateRequestOfCreateMeetingAccount />} />
          <Route path="/meeting/create/completed/:type" element={<CompletedOfCreateMeetingAccount />} />

          {/* 모임통장 상세 */}
          <Route path="/meetingaccount/:id" element={<MeetingAccountDetail />} />
          <Route path="/joinedmeetingaccount/:id" element={<JoinedMeetingAccountDetail />} />
          <Route path="/meetingaccount/management/:id" element={<MeetingAccountManagement />} />
          <Route path="/meetingaccount/management/:id/groupmember" element={<MeetingAccountGroupMember />} />
          <Route path="/meetingaccount/update/:id" element={<MeetingAccountUpdate />} />
          <Route path="/meetingaccount/update/:id" element={<MeetingAccountUpdate />} />

          {/* 모임 초대 */}
          <Route path="/meeting/invite/:code" element={<InvitationOfMeeting />} />
          <Route path="/meeting/invite/:code/info" element={<InviteInfoOfMeeting />} />
          <Route path="/meeting/invite/participated" element={<AlreadyInviteOfMeeting />} />

          {/* 모임통장 채우기 */}
          <Route path="/meeting/:groupId/fill/setmoney" element={<FillSetMoney />}></Route>
          <Route path="/meeting/:groupId/fill/confirm" element={<FillConfirm />}></Route>
          <Route path="/meeting/:groupId/fill/password" element={<FillPassword />}></Route>
          <Route path="/meeting/:groupId/fill/success" element={<FillSuccess />}></Route>

          {/* 통장 내역 */}
          <Route path="/transaction/:accountNo" element={<AccountTransaction />} />
          <Route path="/meetingtransaction/:accountNo/:type" element={<MeetingTransaction />} />
          <Route path="/travelbox/transaction/:accountNo/:type" element={<TravelBoxTransaction />} />

          {/* 외화저금통 생성 */}
          <Route path="/travelbox/create/prepare" element={<TravelBoxCreatePrepare />} />
          <Route path="/travelbox/create/currency" element={<CurrencyInfoOfCreateTravelBox />} />
          <Route path="/travelbox/create/type" element={<SelectTypeOfAutoExchange />} />
          <Route path="/travelbox/create/auto/exchange/rate" element={<AutoCurrencyExchangeRateOfCreateTravelBox />} />
          <Route
            path="/travelbox/create/auto/exchange/amount"
            element={<AutoCurrencyExchangeAmountOfCreateTravelBox />}
          />
          <Route path="/travelbox/create/auto/exchange/completed/:type" element={<CompletedOfCreateAutoExchange />} />

          {/* 외화저금통 자동환전 정보 */}
          <Route path="/travelbox/detail/auto/exchange/:groupId" element={<CurrentAutoExchangeInfo />} />

          {/* 외화저금통 해지 */}
          <Route path="/travelbox/delete/:accountNo/:groupId" element={<DeleteTravelBox />} />
          <Route path="/travelbox/delete/:accountNo/:groupId/password" element={<DeleteTravelBoxPassword />} />
          <Route path="/travelbox/delete/:accountNo/:groupId/completed" element={<CompletedOfDeleteTravelBox />} />

          {/* 카드 개설 */}
          <Route path="/card/:groupId/create/englishname" element={<EnglishNameOfCreateCard />} />
          <Route path="/card/:groupId/create/password/:type" element={<PasswordOfCreateCard />} />
          <Route path="/card/:groupId/create/password/check" element={<CheckPasswordOfCreateCard />} />
          <Route path="/card/:groupId/create/address" element={<AddressOfCreateCard />} />
          <Route path="/card/:groupId/create/completed" element={<CompletedOfCreateCard />} />

          {/* 카드 상세 */}
          <Route path="/card/:groupId" element={<CardDetail />} />

          {/* 카드 내역 */}
          <Route path="/cardtransaction/:groupId" element={<CardTransaction />} />

          {/* 이체 */}
          <Route path="/transfer/selectbank" element={<TransferSelectBank />} />
          <Route path="/transfer/setmoney" element={<TransferSetMoney />} />
          <Route path="/transfer/password" element={<TransferPassword />} />
          <Route path="/transfer/confirm" element={<TransferConfirm />} />
          <Route path="/transfer/success" element={<TransferSuccess />} />

          {/* 환전 */}
          <Route path="/exchange/foreign-currency" element={<Exchange />}></Route>
          <Route path="/exchange/korean-currency" element={<ExchangeKRWFlow />} />
          <Route path="/exchange/account-password-input" element={<AccountPasswordInput />} />
          <Route path="/exchange/exchange-completion" element={<ExchangeCompletion />} />
          {/* <Route path="/selectaccount/:userId" element={<SelectAccount />}></Route> */}

          {/* 환율 */}

          <Route path="/exchangerate/:currencyCode" element={<ExchangeDetail />} />
          <Route path="/exchangerate/forecast-detail" element={<ExchangeRateForecastDetail />} />

          {/* 환율 예측 */}
          {/* <Route path="/exchangerate/forecast" element={<ExchangeRateForecast />} /> */}

          {/* 잔액정산 */}
          <Route path="/settlement/balance/amount/:id" element={<SelectSettlementAmount />}></Route>
          <Route path="/settlement/balance/participants/:id" element={<SettlementInfo />}></Route>
          <Route path="/settlement/editmembers/:type/:id" element={<EditMembers />}></Route>
          <Route path="/settlement/password" element={<PasswordOfBalanceSettlement />}></Route>
          <Route path="/settlement/balance/completed" element={<BalanceSettlementCompleted />}></Route>

          {/* 지출정산 */}
          <Route
            path="/settlement/expenditure/transaction/detail/:id"
            element={<ExpenditureTransactionDetail />}></Route>
          <Route path="/settlement/expenditure/participants/:id" element={<ExpenditureSettlementInfo />}></Route>
          <Route path="/settlement/expenditure/completed" element={<ExpenditureSettlementCompleted />}></Route>
          <Route path="/settlement/expenditure/list/:status" element={<ExpenditureSettlementList />}></Route>
          <Route path="/settlement/expenditure/detail/:id/:status" element={<ExpenditureSettlementDetail />}></Route>

          {/* 모임의 지출정산 내역 */}
          <Route
            path="/settlement/expenditure/group/list/:id/:status"
            element={<ExpenditureSettlementGroupList />}></Route>
          <Route
            path="/settlement/expenditure/group/detail/:id/:status"
            element={<ExpenditureSettlementGroupDetail />}></Route>

          {/* 지출정산 이체 */}
          <Route
            path="/settlement/expenditure/transfer/setMoney/:type"
            element={<SettlementTransferSetMoney />}></Route>
          <Route path="/settlement/expenditure/transfer/confirm/:type" element={<SettlementTransferConfirm />}></Route>
          <Route path="/settlement/expenditure/transfer/password" element={<SettlementTransferPassword />}></Route>
          <Route path="/settlement/expenditure/transfer/success" element={<SettlementTransferSuccess />}></Route>

          {/* 알림 */}
          <Route path="/notification" element={<NotificationList />} />

          {/* 머니로그 */}
          <Route path="/accountBook/create/info/:accountNo" element={<CreateAccountBookInfo />} />

          {/* ver1 */}
          <Route path="/accountcreate" element={<AccountCreate />} />
          <Route path="/accountcreatecomplete" element={<AccountCreateComplete />} />
          <Route path="/generalmeetingaccountcreate" element={<GeneralMeetingAccountCreate />} />
          <Route path="/meetingaccountcreatecomplete" element={<MeetingAccountCreateComplete />} />
          {/* <Route path="/settlement" element={<Settlement />}></Route> */}
          {/* <Routes path="/accounthistory/:accountNo" element={<AccountHistory />} /> */}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
