# 🥘 FoodRiskMap

[식품의약품안전에 관한 법령](https://www.mfds.go.kr/brd/m_203/list.do)을 위반한 식당을 제공하는 서비스입니다.

![foodriskmap](https://github.com/hsyoodev/foodriskmap/assets/102946491/537eb325-59a4-414b-9814-e03a0193b1a0)

Demo👉 https://port-0-foodriskmap-32updzt2alprv5o7g.sel4.cloudtype.app/

## ⛏️ 개발 환경

### Backend

![Static Badge](https://img.shields.io/badge/-Java-%23007396?style=for-the-badge&logo=java&logoColor=white)
![Static Badge](https://img.shields.io/badge/-spring%20boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Static Badge](https://img.shields.io/badge/-spring%20data%20jpa-%236DB33F?style=for-the-badge&logo=springdatajpa&logoColor=white)
![Static Badge](https://img.shields.io/badge/-spring%20security-%236DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Static Badge](https://img.shields.io/badge/-thymeleaf-%23005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Static Badge](https://img.shields.io/badge/-mariadb-%23003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Static Badge](https://img.shields.io/badge/-apache%20maven-%23C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

### Infra

![Static Badge](https://img.shields.io/badge/-cloudetype-%23000000?style=for-the-badge&logo=cloudetype&logoColor=white)

### IDE

![Static Badge](https://img.shields.io/badge/-intellij-%23000000?style=for-the-badge&logo=intellijidea&logoColor=white)

### API

![Static Badge](https://img.shields.io/badge/-web%20dynamic%20map-%2303C75A?style=for-the-badge&logo=naver&labelColor=abcdef)
![Static Badge](https://img.shields.io/badge/-geocoding-%2303C75A?style=for-the-badge&logo=naver&labelColor=abcdef)

## 📁 패키지 구조

```bash
📦foodriskmap
 ┣ 📂api
 ┣ 📂config
 ┣ 📂controller
 ┣ 📂entity
 ┣ 📂interceptor
 ┣ 📂repository
 ┣ 📂service
 ┗ 📜FoodriskmapApplication.java
```

## 👀 주요 기능

### 식당 검색

* 사용자는 지도를 통해 식품의약품안전에 관한 법령을 위반한 식당 정보를 얻을 수 있다.
* 사용자는 자신의 현재 위치 및 주변 반경을 확인할 수 있다.

  ![map](https://github.com/hsyoodev/foodriskmap/assets/102946491/d9e08e22-8891-4b85-953e-1fffcdc30df1)

### 회원가입 및 로그인

* 사용자는 이메일, 비밀번호를 입력하여 회원가입을 할 수 있다.
* 이미 존재하는 이메일로의 중복 가입을 방지한다.
* 사용자는 이메일과 비밀번호를 입력하여 로그인을 할 수 있다.
* 사용자가 입력한 정보의 유효성을 검사하여 올바른 정보를 입력하도록 안내한다.
* 로그인 실패 시 사용자에게 오류 메세지를 보여주고, 다시 로그인을 시도하도록 안내한다.
* 로그인 성공 시 사용자에게 세션을 부여하여 로그인 상태를 유지한다.

  ![login](https://github.com/hsyoodev/foodriskmap/assets/102946491/f22691bc-4418-407b-9322-af45cd1da162)

### 제보 게시판

* 사용자는 자신이 발견한 식품의약품안전에 관한 법령을 위반한 식당을 제보할 수 있다.

![board](https://github.com/hsyoodev/foodriskmap/assets/102946491/08298591-add0-468e-8506-19c1bae68ce6)
