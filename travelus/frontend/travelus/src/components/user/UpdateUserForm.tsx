import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editUserInformation } from "../../redux/userInformationSlice";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import { IoIosArrowForward } from "react-icons/io";
import Loading from "../loading/Loading";

interface UpdateUserFormProps {
  inputs: {
    id: string;
    name: string;
    birth: string;
    phone: string;
    address: string;
  };
  setInputs: React.Dispatch<
    React.SetStateAction<{
      id: string;
      name: string;
      birth: string;
      phone: string;
      address: string;
    }>
  >;
}

const UpdateUserForm: React.FC<UpdateUserFormProps> = ({ inputs, setInputs }) => {
  const userData = useSelector((state: RootState) => state.userInformation.UserInfo);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(true);

  const [errors, setErrors] = useState({
    id: false,
    name: false,
    birth: false,
    phone: false,
    address: false,
  });

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
      dispatch(
        editUserInformation({
          ...userData,
          name: inputs.name,
          birth: inputs.birth,
          phone: inputs.phone,
          address: inputs.address,
        })
      );
    }, 500); // 1초 후에 로딩 상태 해제

    return () => clearTimeout(timer); // 컴포넌트 언마운트 시 타이머 클리어
  }, [inputs, dispatch, userData]);

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const isKorean = /^[가-힣]+$/.test(value);

    switch (id) {
      case "name":
        error = !isKorean || value.trim() === "" || value.length < 2;
        break;
      case "birthday":
        // 생년월일은 YYYYMMDD 형식이어야 하며, 날짜 유효성 검사
        const dateRegex = /^\d{8}$/;
        const isValidDateFormat = dateRegex.test(value);
        break;
      case "phone":
        // 휴대폰 번호는 11자리 숫자만 허용
        const phoneRegex = /^\d{11}$/;
        error = !phoneRegex.test(value);
        break;
      case "address":
        error = value.trim() === "";
        break;
      default:
        break;
    }
    return error;
  };

  const formatBirthday = (value: string) => {
    // Remove any non-digit characters
    const cleanedValue = value.replace(/\D/g, "");

    // Format as YYYY-MM-DD
    if (cleanedValue.length <= 4) {
      return cleanedValue;
    } else if (cleanedValue.length <= 6) {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4)}`;
    } else {
      return `${cleanedValue.slice(0, 4)}-${cleanedValue.slice(4, 6)}-${cleanedValue.slice(6, 8)}`;
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    const formattedValue = id === "birth" ? formatBirthday(value) : value;

    setInputs((prev) => ({ ...prev, [id]: formattedValue }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  const validateInputs = () => {
    const newErrors = {
      id: handleValidation("id", inputs.id),
      name: handleValidation("name", inputs.name),
      birth: handleValidation("birthday", inputs.birth),
      phone: handleValidation("phone", inputs.phone),
      address: handleValidation("address", inputs.address),
    };
    setErrors(newErrors);
    return !Object.values(newErrors).some((error) => error);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateInputs()) {
      // 유효성 검사 통과 시 처리 로직
      console.log("Form submitted:", inputs);
    }
  };

  if (isLoading) {
    return <Loading />;
  }

  return (
    <>
      <Box
        className="w-full flex flex-col justify-center items-center space-y-6"
        component="form"
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit}>
        {inputs && (
          <>
            <TextField
              required
              className="w-full h-14"
              id="id"
              label="아이디"
              variant="outlined"
              value={inputs.id}
              onChange={handleChange}
              error={errors.id}
              helperText={errors.id ? "유효한 이메일을 입력하세요." : ""}
              disabled={true}
              sx={{
                "& .MuiOutlinedInput-root": {
                  backgroundColor: "white",
                  height: "50px",
                },
                "& .MuiFormHelperText-root": {
                  fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
                  marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
                },
              }}
            />
            <TextField
              required
              className="w-full h-14"
              id="name"
              label="성명"
              variant="outlined"
              value={inputs.name}
              onChange={handleChange}
              error={errors.name}
              helperText={errors.name ? "한글 2~8자" : ""}
              sx={{
                "& .MuiOutlinedInput-root": {
                  backgroundColor: "white",
                  height: "50px",
                },
                "& .MuiFormHelperText-root": {
                  fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
                  marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
                },
              }}
            />

            <TextField
              required
              className="w-full h-14"
              id="birth"
              label="생년월일"
              variant="outlined"
              value={inputs.birth}
              onChange={handleChange}
              error={errors.birth}
              helperText={errors.birth ? "YYYYMMDD 형식으로 입력해주세요." : ""}
              sx={{
                "& .MuiOutlinedInput-root": {
                  backgroundColor: "white",
                  height: "50px",
                },
                "& .MuiFormHelperText-root": {
                  fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
                  marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
                },
              }}
            />

            <TextField
              required
              className="w-full h-14"
              id="phone"
              label="휴대폰 번호"
              variant="outlined"
              value={inputs.phone}
              onClick={() => {
                navigate("/userupdate/phone");
              }}
              onChange={handleChange}
              error={errors.phone}
              helperText={errors.phone ? "숫자만 입력해주세요." : ""}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IoIosArrowForward style={{ cursor: "pointer" }} />
                  </InputAdornment>
                ),
              }}
              sx={{
                "& .MuiOutlinedInput-root": {
                  backgroundColor: "white",
                  height: "50px",
                  cursor: "pointer",
                },
                "& input": {
                  cursor: "pointer", // 입력 필드에 커서 스타일을 적용
                },
                "& .MuiFormHelperText-root": {
                  fontSize: "0.7rem", // 도움말 텍스트 크기 조정 (예: 12px)
                  marginTop: "2px", // 도움말 텍스트의 위쪽 마진 조정
                },
              }}
            />

            <TextField
              required
              className="w-full h-14"
              id="address"
              label="주소"
              variant="outlined"
              value={inputs.address}
              onClick={() => {
                navigate("/userupdate/address");
              }}
              onChange={handleChange}
              error={errors.address}
              helperText={errors.address ? "주소를 입력해주세요." : ""}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IoIosArrowForward style={{ cursor: "pointer" }} />
                  </InputAdornment>
                ),
              }}
              sx={{
                "& .MuiOutlinedInput-root": {
                  backgroundColor: "white",
                  height: "50px",
                  cursor: "pointer",
                },
                "& input": {
                  cursor: "pointer",
                },
                "& .MuiFormHelperText-root": {
                  fontSize: "0.7rem",
                  marginTop: "2px",
                },
              }}
            />
            <div className="w-full flex justify-end">
              <button
                onClick={() => {
                  navigate("/userupdate/password");
                }}
                className="w-auto px-5 text-[#1429A0]">
                비밀번호 변경
              </button>
            </div>
          </>
        )}
      </Box>
    </>
  );
};

export default UpdateUserForm;
