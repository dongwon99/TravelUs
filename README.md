# :family: TravelUs

### AI 기술 기반의 해외여행 모임통장 서비스

![main](/uploads/e4f48c8869bfd1deca335ff2eb480928/main.png)

- 배포 링크: https://j11d209.p.ssafy.io
- 모바일 기반의 PWA를 적용한 어플리케이션이기 때문에 모바일 환경에서 사용을 권장합니다.

## :calendar: 기간

- 2024.08.19 ~ 2024.10.11

## 목차

1. :golf: **프로젝트 소개**

2. :star: **주요 기능**

3. :closed_book:**시스템 아키텍처**

4. :file_folder: **기술 스택**

5. :pencil: **ERD**

6. 💻 ** 빌드 및 실행방법 **

7. :tv: **구현 화면(사용 플로우)**
   
   :one: 회원가입 및 로그인
   
   :two: 메인 페이지
   
   :three: 모임통장 개설 
   
   :four: 환율 확인 
   
   :five: 이체 기능  
   
   :six: 환전 기능
   
   :seven: 정산 기능 
   
   :eight: 이체 기록 확인
   
   :nine: 가계부 기능

8. :+1: **멤버**

## :golf: 프로젝트 소개

#### 희망 환율에 자동 환전 기능을 제공하는 외화 모임 통장과, 해외여행 시 다양한 부가 혜택을 누릴 수 있는 혁신적인 금융 서비스

#### 환율 예측 추천 서비스에 따라 사용자가 원하는 시점에 환전이 가능하며, 가계부 및 자동 정산 기능을 통해 여행 이후에 돈 관리를 손쉽게 관리가 가능하게 함

## :star: 주요 기능

### 모임 통장에 돈을 모아 희망환율에 자동으로 외화저금통에 환전이 되는 서비스

### 여행 중에 지출 내역을 한 눈에 볼 수 있는 '머니로그'

### 여행 이후에는 개별 지출에 대한 정산로직을 구현하여 정산

## :closed_book:시스템 아키텍처

![architecture](/uploads/9f6becf0d109aca7d95f6a25625ba843/architecture.png)

## :file_folder: 기술 스택

##### FrontEnd

- React

- Redux

- Javascript

- Node.js

- Tailwind.css

- MUI

- daisyUI

---

##### BackEnd

- Sping

- Spring Security

- JWT

- JPA

- QueryDsl

-----

##### DB

- MySql

- Redis

- S3

---



##### Infra

- Jenkins

- Docker

- DockerHub

- Nginx

- EC2

-----

## :pencil: ERD

![GOOFY](/uploads/3c6cf605a81e7f6c3d6ac3086799106f/GOOFY.png)

## :tv: 구현 화면

#### :one: 회원가입 및 로그인
- 회원가입
![회원가입](/uploads/0169a87efa790bef9bb02ea20af73fed/회원가입.png)
![주소_입력](/uploads/55079de8794d6c86403bc9af26f87bb9/주소_입력.png)
- 로그인
![로그인](/uploads/ee26a2916ac9f1d5ba2b7f4d40b8340f/로그인.gif)

#### :two: 메인 페이지
![최초_입장_메인](/uploads/8d5903dbed6c43a4705c09686ab3c69f/최초_입장_메인.gif)

#### :three: 통장 개설
##### 입출금 통장 개설
![입출금_통장_개설](/uploads/c2740b45868212324f37b14bf2732da4/입출금_통장_개설.gif)

##### 모임 통장 개설
![모임_통장_개설](/uploads/d1693dd06ed5c2091c4ae0229b34dcf6/모임_통장_개설.gif)

##### 외화저금통 생성
![외화저금통_생성](/uploads/4873ce568957a5308af4b22bb00cba18/외화저금통_생성.gif)

### :four: 이체 관련
#### 모임 통장 입금
![모임_통장_입금](/uploads/dfa795c152cfeefb707d401ef741e4ae/모임_통장_입금.gif)

#### 외화로 환전하기
![외화로_환전하기](/uploads/fdd205ddb907c90a1253d0dfd45b2ec2/외화로_환전하기.gif)

#### 원화로 환전하기
![원화로_환전하기](/uploads/1a43f48d7f0bf869a2aa3df6ee509934/원화로_환전하기.gif)

### :five: 카드 신청
![카드_신청](/uploads/fa8775c155dbb144394575abfee21974/카드_신청.gif)

### :six: 모임원 관리 페이지
![관리_페이지](/uploads/58745c6cbe0dafb4f9178451bc14f845/관리_페이지.gif)

### :seven: 환율 및 환율 예측 조회
![환율_및_환율_예측_조회](/uploads/4ae3b68e9364eb4cca3a127e2f122783/환율_및_환율_예측_조회.gif)

### :eight: 머니로그 사용
![머니_로그_사용](/uploads/7274466c8b12a04e4ea2393b74d1f35d/머니_로그_사용.gif)

### :nine: 정산하기


# :+1: 멤버
![멤버_사진_1](/uploads/454613d331645a3029e41475996643c2/멤버_사진_1.png)
![멤버_사진_2](/uploads/18c54f3036129511550c3a23d14e245c/멤버_사진_2.png)
