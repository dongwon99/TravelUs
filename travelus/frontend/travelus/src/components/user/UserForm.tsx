import { useState, useEffect } from "react";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { userApi } from "../../api/user";

interface InputState {
  file: File | null;
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
  phone: string;
  address: string;
  verificationCode?: string;
  accountPassword: string;
}

interface Props {
  inputs: InputState;
  setInputs: (value: React.SetStateAction<InputState>) => void;
  setIsFormValid: (value: boolean) => void;
}

const UserForm = ({ inputs, setInputs, setIsFormValid }: Props) => {
  const [errors, setErrors] = useState({
    email: false,
    password: false,
    confirmPassword: false,
    name: false,
    birthday: false,
    phone: false,
    address: false,
    verificationCode: false,
    accountPassword: false,
  });
  
  const handleSendVerificationCode = async () => {
    try {
      const formattedValue = inputs.phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
      const response = await userApi.fetchSendSmsValidation(formattedValue);
      if (response.status === 200) {
        alert("인증 코드가  발송되었습니다. 확인해주세요.");
      }
    } catch (error) {
      console.error("Error sending verification code:", error);
      alert("인증 코드 발송 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const handleValidation = (id: string, value: string) => {
    let error = false;

    const hasEnglishLetters = /[a-zA-Z]/.test(value);
    const hasNumbers = /\d/.test(value);

    switch (id) {
      case "email":
        error = !value.includes("@");
        break;
      case "password":
        error = value.length < 6;
        break;
      case "confirmPassword":
        error = value !== inputs.password; // 패스워드 확인이 일치하는지 확인
        break;
      case "name":
        error = hasEnglishLetters || hasNumbers || value.trim() === "";
        break;
      case "birthday":
        // 생년월일은 YYYYMMDD 형식이어야 하며, 날짜 유효성 검사
        const dateRegex = /^\d{8}$/;
        const isValidDateFormat = dateRegex.test(value);
        if (isValidDateFormat) {
          const year = parseInt(value.substring(0, 4), 10);
          const month = parseInt(value.substring(4, 6), 10);
          const day = parseInt(value.substring(6, 8), 10);
          // 간단한 날짜 유효성 검사
          const isValidDate = month >= 1 && month <= 12 && day >= 1 && day <= 31;
          error = !isValidDate;
        } else {
          error = true;
        }
        break;
      case "phone":
        // 휴대폰 번호는 11자리 숫자만 허용
        const phoneRegex = /^\d{11}$/;
        error = !phoneRegex.test(value);
        break;
      case "verificationCode":
        // 인증번호는 6자리 알파벳이나 숫자만 허용
        const codeRegex = /^[a-zA-Z0-9]{6}$/; 
        error = !codeRegex.test(value);
        break;
      case "address":
        error = value.trim() === "";
        break;
      case "accountPassword":
        // 입출금 계좌 비밀번호는 4자리 숫자만 허용
        const accountPasswordRegex = /^\d{4}$/;
        error = !accountPasswordRegex.test(value);
        break;
      default:
        break;
    }
    return error;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    setInputs((prev) => ({ ...prev, [id]: value }));

    // 입력이 변경될 때마다 유효성 검사를 실행하고 에러 상태 업데이트
    const error = handleValidation(id, value);
    setErrors((prev) => ({ ...prev, [id]: error }));
  };

  useEffect(() => {
    // 모든 필드가 유효한지 확인
    const allFieldsValid = Object.values(errors).every((error) => !error);
    const allFieldsFilled = Object.values(inputs).every((value) => value !== "");

    setIsFormValid(allFieldsValid && allFieldsFilled);
  }, [errors, inputs]);


  return (
    <>
      <Box
        className="w-full flex flex-col justify-center items-center space-y-5"
        component="form"
        noValidate
        autoComplete="off">
        <TextField
          required
          className="w-full h-14"
          id="email"
          label="이메일"
          variant="outlined"
          value={inputs.email}
          onChange={handleChange}
          error={errors.email}
          helperText={errors.email ? "유효한 이메일을 입력하세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
        <TextField
          required
          className="w-full h-14"
          id="password"
          label="사용자 암호"
          variant="outlined"
          type="password"
          value={inputs.password}
          onChange={handleChange}
          error={errors.password}
          helperText={errors.password ? "암호는 최소 6자 이상이어야 합니다." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
        <TextField
          required
          className="w-full h-14"
          id="confirmPassword"
          label="사용자 암호 확인"
          variant="outlined"
          type="password"
          value={inputs.confirmPassword}
          onChange={handleChange}
          error={errors.confirmPassword}
          helperText={errors.confirmPassword ? "암호가 일치하지 않습니다." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
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
          helperText={errors.name ? "성함은 한글로 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
        <TextField
          required
          className="w-full h-14"
          id="birthday"
          label="생년월일"
          variant="outlined"
          value={inputs.birthday}
          onChange={handleChange}
          error={errors.birthday}
          helperText={errors.birthday ? "YYYYMMDD 형식으로 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
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
          onChange={handleChange}
          error={errors.phone}
          helperText={errors.phone ? "숫자만 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />

        <div className="w-full h-16 pl-2 flex items-start justify-between">
          <TextField
             value={inputs.verificationCode}
             onChange={handleChange}
             error={errors.verificationCode}
            className="w-[70%] h-14"
            id="verificationCode"
            label="인증 번호"
            variant="standard"
            sx={{
              "& .MuiOutlinedInput-root": {
                backgroundColor: "white",
                height: "50px",
              },
              "& .MuiFormHelperText-root": {
                fontSize: "0.7rem",
                marginTop: "2px",
              },
            }}
          />
          <Button
            onClick={handleSendVerificationCode}
            className="h-14 w-20 flex flex-col"
            variant="contained"
            sx={{
              backgroundColor: "#1A73E8", // 버튼의 배경색
              color: "white", // 텍스트 색상
              padding: "4px",
              // "&:hover": {
              //   backgroundColor: "#0036D4", // hover 상태에서의 배경색
              // },
            }}>
            <p className="text-xs font-semibold">인증번호</p>
            <p className="text-xs font-semibold">발송</p>
          </Button>
        </div>

        <TextField
          required
          className="w-full h-14"
          id="address"
          label="주소"
          variant="outlined"
          value={inputs.address}
          onChange={handleChange}
          error={errors.address}
          helperText={errors.address ? "주소를 입력해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />

        <TextField
          required
          className="w-full h-14"
          id="accountPassword"
          label="입출금 계좌 비밀번호"
          type="password"
          variant="outlined"
          value={inputs.accountPassword}
          onChange={handleChange}
          error={errors.accountPassword}
          helperText={errors.accountPassword ? "입출금 계좌 비밀번호를 4자리로 설정해주세요." : ""}
          sx={{
            "& .MuiOutlinedInput-root": {
              backgroundColor: "white",
              height: "50px",
            },
            "& .MuiFormHelperText-root": {
              fontSize: "0.7rem",
              marginTop: "2px",
            },
          }}
        />
      </Box>
    </>
  );
};

export default UserForm;
