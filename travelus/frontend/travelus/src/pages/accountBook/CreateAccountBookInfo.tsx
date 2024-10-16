import React, { useEffect, useRef, useState } from "react";
import { accountBookApi } from "../../api/accountBook";
import { BuyItemInfo } from "../../types/accountBook";
import { IoIosArrowBack } from "react-icons/io";
import { accountApi } from "../../api/account";
import { useNavigate, useParams } from "react-router";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";
import idcardLoadingAnimation from "../../lottie/idcardLoadingAnimation.json";
import BuyStoreInputMui from "../../components/accountBook/inputField/BuyStoreInputMui";
import BuyPaidInputMui from "../../components/accountBook/inputField/BuyPaidInputMui";
import BuyItemNameInputMui from "../../components/accountBook/inputField/BuyItemNameInputMui";
import BuyItemPriceInputMui from "../../components/accountBook/inputField/BuyItemPriceInputMui";
import BuyItemQuantityInputMui from "../../components/accountBook/inputField/BuyItemQuantityInputMui";
import BuyItemTable from "../../components/accountBook/BuyItemTable";
import DateInputMui from "../../components/accountBook/inputField/DateInputMui";
import BuyCurrency from "../../components/accountBook/inputField/BuyDateInputMui";

const CreateAccountBookInfo = () => {
  const navigate = useNavigate();
  const { accountNo } = useParams();
  const [currency, setCurrency] = useState("");
  const [selectCurrency, setSelectCurrency] = useState("");
  const [buyDate, setBuyDate] = useState("");
  const [buyStore, setBuyStore] = useState("");
  const [buyAddress, setBuyAddress] = useState("");
  const [paid, setPaid] = useState("");
  const [buyItems, setBuyItems] = useState<BuyItemInfo[]>([]);

  const [itemName, setItemName] = useState("");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState(0);

  const [receiptFile, setReceiptFile] = useState<File | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isAccountLoading, setAccountIsLoading] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const loadingText = ["영수증을 확인하고 있어요", "거의 다 확인했어요", "조금만 더 기다려주세요"];
  const [loadingTextIdx, setLoadingTextIdx] = useState(0);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");

    return `${year}-${month}-${day}`;
  };

  const fetchAccountInfo = async (accountNo: string) => {
    try {
      setAccountIsLoading(true);
      const response = await accountApi.fetchSpecificAccountInfo(accountNo);
      if (response.data.moneyBoxDtos.length === 2) {
        setCurrency(response.data.moneyBoxDtos[1].currencyCode);
      }
    } catch (error) {
      console.log("accountApi의 fetchAllAccountInfo : ", error);
    } finally {
      setAccountIsLoading(false);
    }
  };

  const handleReceiptUpload = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setReceiptFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  };

  const handleAddItem = () => {
    if (!quantity) return;

    let data = { item: itemName, price: Number(price), quantity: quantity };
    if (buyItems) {
      setBuyItems([...buyItems, data]);
    }
    setItemName("");
    setPrice("");
    setQuantity(0);
  };

  // 머니로그 등록
  const handleCreateAccountBook = async () => {
    if (!accountNo) return;

    const data = {
      accountNo: accountNo,
      store: buyStore,
      paid: Number(paid),
      items: buyItems,
      transactionAt: buyDate,
      address: buyAddress,
      currency: selectCurrency,
    };
    console.log(data);
    try {
      const response = await accountBookApi.createAccountBook(data);
      if (response.status === 200) {
        navigate("/accountbookdetail", { state: { accountNo } });
      }
    } catch (error) {
      console.log("accountBookApi의 createAccountBook : ", error);
    }
  };

  // 영수증 인식 후 데이터 가져오기
  const handleReceiptUploadInfo = async (fileType: string) => {
    setIsLoading(true);
    const formData = new FormData();
    if (receiptFile) {
      formData.append("file", receiptFile);
      formData.append("format", fileType);
      formData.append("lang", ""); // "" : 영어
    }

    try {
      const response = await accountBookApi.fetchReceiptInfo(formData);
      if (response.status === 200) {
        const data = response.data;
        setBuyStore(data.store);
        setBuyAddress(data.address);
        setPaid(data.paid);
        setBuyDate(formatDate(data.transactionAt));
        setBuyItems(data.items);
        setReceiptFile(null);
      }
    } catch (error) {
      console.log("accountBookApi의 fetchReceiptInfo : ", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (accountNo) {
      fetchAccountInfo(accountNo);
    }
  }, []);

  // 영수증 업로드 시
  useEffect(() => {
    if (receiptFile) {
      const fileType = receiptFile.type.replace("image/", "");
      handleReceiptUploadInfo(fileType);
    }
  }, [receiptFile]);

  // 사용금액 계산
  useEffect(() => {
    if (buyItems.length === 0) return;

    const totalPaid = buyItems.reduce((acc, cur) => acc + cur.price * cur.quantity, 0);
    setPaid(totalPaid.toFixed(2).toString());
  }, [buyItems]);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setLoadingTextIdx((prev) => (prev + 1) % 3);
    }, 3000);

    return () => clearInterval(intervalId); // 컴포넌트가 언마운트될 때 interval 정리
  }, [isLoading]);

  if (isAccountLoading) {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

  return (
    <div className="w-full h-full pb-8 flex flex-col justify-between">
      <div>
        <div className="p-5 bg-white flex items-center sticky top-0 z-50">
          <IoIosArrowBack className="text-2xl" onClick={() => navigate("/accountbookdetail")} />
        </div>

        <div className="p-5 pb-32 grid gap-8 overflow-y-auto">
          <div className="flex justify-between">
            <p className="text-2xl font-semibold">
              결제 정보를
              <br />
              입력해주세요
            </p>

            <div className="grid grid-rows-2">
              <button
                className={`row-start-2 p-2 text-sm text-[#565656] font-semibold bg-[#D8E3FF] rounded-md ${
                  isLoading ? "opacity-50" : ""
                }`}
                onClick={() => handleReceiptUpload()}
                disabled={isLoading}>
                영수증으로 작성
              </button>

              <input
                type="file"
                ref={fileInputRef}
                style={{ display: "none" }}
                onChange={handleFileChange}
                accept="image/*"
              />
            </div>
          </div>

          {isLoading ? (
            <div className="h-full py-5 flex flex-col justify-center items-center space-y-3">
              <p className="text-lg font-semibold">{loadingText[loadingTextIdx]}</p>
              <Lottie className="w-1/2" animationData={idcardLoadingAnimation} />
            </div>
          ) : (
            <>
              <div className="flex flex-col space-y-3">
                <BuyCurrency
                  currency={currency}
                  selectCurrency={selectCurrency}
                  setSelectCurrency={setSelectCurrency}
                />
                <DateInputMui labelName="결제일" date={buyDate} setDate={setBuyDate} />
                <BuyStoreInputMui buyStore={buyStore} setBuyStore={setBuyStore} />
                <BuyPaidInputMui paid={paid} setPaid={setPaid} />
              </div>

              <BuyItemTable buyItems={buyItems} />

              <div className="flex flex-col space-y-2">
                <div className="flex justify-between items-center">
                  <p className="font-semibold">무엇을 구매했나요?</p>
                  <button
                    className={`px-5 py-2 text-sm text-[#565656] font-semibold bg-[#D8E3FF] rounded-md ${
                      itemName === "" || quantity === 0 ? "opacity-50" : ""
                    }`}
                    onClick={() => handleAddItem()}
                    disabled={itemName === "" || quantity === 0}>
                    추가
                  </button>
                </div>

                <BuyItemNameInputMui itemName={itemName} setItemName={setItemName} />

                <div className="flex space-x-3">
                  <BuyItemPriceInputMui price={price} setPrice={setPrice} />
                  <BuyItemQuantityInputMui quantity={quantity} setQuantity={setQuantity} />
                </div>
              </div>
            </>
          )}
        </div>
      </div>

      <div className="w-full p-5 pb-8 bg-white fixed bottom-0 z-50">
        <button
          className={`w-full h-14 text-lg rounded-xl tracking-wide ${
            selectCurrency === "" || buyDate === "" || buyStore === "" || paid === ""
              ? "text-[#565656] bg-[#E3E4E4]"
              : "text-white bg-[#1429A0]"
          }`}
          onClick={() => handleCreateAccountBook()}
          disabled={buyStore === "" || paid === "" || buyItems.length === 0}>
          등록
        </button>
      </div>
    </div>
  );
};

export default CreateAccountBookInfo;
