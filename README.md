# 📚 Catch Study

스터디카페 자리를 예약할 수 있는 웹 서비스

https://catch-study.kro.kr/

스터디카페에 갔는데 자리가 없어 급하게 주변에 있는 다른 스터디카페를 찾아보신 경험이 있으신가요? 헛걸음하는 것을 방지하기 위해 스터디카페 좌석을 예약할 수 있는 서비스를 기획하게 되었습니다.

### 이 서비스를 이용하게 된다면

1. 이용자는 스터디카페 좌석 현황을 미리 파악하여 **시간을 절약**할 수 있습니다.
2. 관리자는 이용자들과의 1:1 채팅을 통해 **매장을 효율적으로 관리**할 수 있습니다.

# 🗓️ 개발 기간

전체 개발 기간: 2024-06-25 ~ 2024-08-09 (7주)

# 🙋‍♂️ 팀원 소개

|<img src="https://avatars.githubusercontent.com/u/79002373?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/101779861?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/62873417?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/48711163?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|:-:|
|FE 팀원: 조현정<br/>[@HyunJungJo98](https://github.com/HyunJungJo98)|FE 팀원: 유의진<br/>[@ejinn1](https://github.com/ejinn1)|BE 팀원: 이다은<br/>[@llynn97](https://github.com/llynn97)|BE 팀원: 김태훈<br/>[@TaeHoon0](https://github.com/TaeHoon0)|

# 💫 담당 기능

### 이다은
- 스터디룸,좌석 예약
- 카카오페이 결제/취소
- 현재 예약 내역 조회
- 마이페이지 예약 내역 조회
- 스터디룸 자동 입실/퇴실 처리
- 좌석 입실/퇴실 처리

### 김태훈
- 스터디카페 목록,상세 정보 조회
- 카카오/구글 로그인
- 마이페이지 회원 정보 조회
- 로그아웃, 회원 탈퇴
- 관리자 업장 정보 입력/수정/조회
- 관리자/사용자 1:1 채팅

# 🛠️ 기술 스택

## Front-End

| 분류          | 기술 스택                                 |
| ------------- | ----------------------------------------- |
| Web           | React, TypeScript, jotai, tailwind, stomp |
| CI/CD         | Github Actions                            |
| Cloud Service | AWS EC2, AWS S3, AWS CodeDeploy, Nginx    |
| Common        | ESLint, Prettier                          |
| Design        | Figma                                     |

## Back-End

| 분류          | 기술 스택                                                                          |
| ------------- |--------------------------------------------------------------------------------|
| frameworks    | Spring Boot, Spring Data JPA, Spring Security, Spring Quartz, STOMP, OAuth 2.0 |
| CI / CD       | Github Actions                                                                 |
| DataBase      | MySQL, Redis                                                                   |
| Cloud Service | AWS EB, AWS EC2, AWS Route53, AWS RDS, AWS S3                                  |

# 🧬 프로젝트 구조

![image](https://github.com/user-attachments/assets/3f76b3cc-f193-4949-8e86-8bba07866b81)

# 🌟 기능

## 일반 사용자

- 스터디카페 목록 표시 : 시, 군/구, 동을 선택하면 해당 주소에 위치한 스터디카페 목록이 표시된다.

![image](https://github.com/user-attachments/assets/ef5a5e9e-e1aa-4cf9-aec1-cf528beb7f89)

- 스터디카페 상세 정보 표시 : 선택한 스터디카페의 상세 정보가 표시된다.

![image](https://github.com/user-attachments/assets/ef3d2ac8-9e5f-47e4-be26-5adeebdcdd12)

- 로그인 : 카카오 계정과 구글 계정으로 회원가입을 할 수 있다.

![image](https://github.com/user-attachments/assets/684731b8-e9dd-4cdb-a85e-e42e5a1ecb6a)

- 좌석 예약 : 사용 가능한 좌석과 시간을 선택하여 예약 할 수 있다.

![image](https://github.com/user-attachments/assets/6ba57f99-addb-47c0-a584-0d5c6415fed8)
![image](https://github.com/user-attachments/assets/edf3ff31-c5d4-498d-b9e6-78fccd749ecc)

- 스터디룸 예약 : 예약 가능한 시간을 선택하여 예약 할 수 있다.

![image](https://github.com/user-attachments/assets/cefe374f-fca4-458c-885c-5268b06467e6)
![image](https://github.com/user-attachments/assets/d5979e7e-220e-42cb-9c66-5c93af95238b)

- 결제 : 카카오 페이를 사용하여 간편 결제를 할 수 있다.

![image](https://github.com/user-attachments/assets/a61098b5-2a0d-4196-8a87-638cdf529764)
![image](https://github.com/user-attachments/assets/11c41aa3-9d4d-4c4d-84a5-53162bb45759)

- 현재 예약 내역 확인 : 현재 예약한 내용을 확인하고 인증번호를 통해 출입 할 수 있다.

![image](https://github.com/user-attachments/assets/60c72356-651a-42a1-bc4e-a669af39c94a)

- 관리자 1:1 문의 : ‘관리자 1:1 문의’ 버튼을 누르면 해당 스터디카페 관리자와의 채팅방이 개설된다.

![image](https://github.com/user-attachments/assets/2d27887b-6d49-4b81-bea0-28231267624e)

- 채팅 : 해당 스터디카페의 관리자에게 실시간으로 문의를 남길 수 있다.

![image](https://github.com/user-attachments/assets/76898536-f94f-48a6-8a52-13f413310f44)

- 예약 내역 조회 : 처음에 최근 30개 예약 내역을 표시하고 이후 날짜를 선택하면 해당 기간 안의 예약 내역을 표시한다.

![image](https://github.com/user-attachments/assets/7c4c2c8c-c8b9-4ab2-a73e-b03a9754da08)

## 스터디카페 관리자

- 스터디카페 정보 조회 : 현재 자신이 운영 중인 스터디카페의 상태를 확인할 수 있다.

![image](https://github.com/user-attachments/assets/33f41369-79be-47b6-83eb-5f9beb564353)

- 스터디카페 정보 입력 : 새로운 스터디카페를 등록할 수 있다.

![image](https://github.com/user-attachments/assets/3564d5c3-9c47-439f-8b94-463cc86799e2)

- 스터디카페 정보 상세보기 : 상세보기 버튼을 누르면 해당 스터디카페의 상세정보를 확인할 수 있다.

![image](https://github.com/user-attachments/assets/85c07bbb-3523-4779-b0b2-cf3f3d78b6b8)

- 스터디카페 정보 수정 : 상세보기 페이지에서 수정하기 버튼을 누르면 해당 스터디카페의 정보를 수정할 수 있다.

![image](https://github.com/user-attachments/assets/d609a51c-d7de-4336-b014-65a527fe1552)

- 채팅 : 스터디카페를 이용 중인 고객에게 실시간으로 문의를 답변할 수 있다.

![image](https://github.com/user-attachments/assets/21de07eb-dd2e-4591-a44b-b3ced37ae690)
