# 🐾 Pet-Life-Cycle Backend

[![Build](https://img.shields.io/github/actions/workflow/status/your-username/pet-life-cycle-backend/gradle.yml?branch=main)](https://github.com/your-username/pet-life-cycle-backend/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](./LICENSE)
[![Coverage](https://img.shields.io/badge/Coverage-80%25-green)](#)

`Pet-Life-Cycle`은 반려동물의 **생애주기 전반을 기록하고 관리**할 수 있는 백엔드 프로젝트입니다.  
사용자는 반려동물의 정보와 건강 기록을 체계적으로 관리하고, 예방접종과 데일리 미션을 통해 **더 깊은 유대감**을 형성할 수 있습니다.

---

## 🌟 주요 기능 (Features)

- 🐾 **반려동물 정보 관리** — 이름, 품종, 생년월일 등의 기본 정보를 등록 및 수정  
- 📊 **건강 기록 및 리포트** — 체중, 건강 이상 기록 및 주간·월간 리포트 제공  
- 💉 **예방 접종 관리** — 필수 접종 내역 등록 및 일정 관리  
- ☁️ **사진 및 동영상 업로드** — AWS S3 연동을 통한 안전한 저장 및 공유  
- 🤖 **AI 챗봇** — OpenAI API 기반 반려동물 양육 Q&A  
- 🎯 **데일리 미션** — 매일 미션 수행을 통한 교감 형성  

---

## 🛠️ 기술 스택 (Tech Stack)

| 분야             | 기술                                                                 |
|------------------|----------------------------------------------------------------------|
| **Backend**      | Java 17 · Spring Boot 3.5.5 · Spring Data JPA · Spring WebFlux · Validation · Lombok |
| **Database**     | MySQL · Redis                                                        |
| **Cloud**        | AWS S3                                                               |
| **Testing**      | JUnit 5                                                              |

---



## 🚀 시작하기 (Getting Started)
```bash
1️⃣ 프로젝트 클론 (Clone)

git clone https://github.com/your-username/pet-life-cycle-backend.git
cd pet-life-cycle-backend
⚠️ your-username을 실제 GitHub 계정명으로 변경하세요.



2️⃣ 환경 변수 설정 (Configuration)

src/main/resources/application.yaml에 아래 설정을 추가하세요.

yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your-database-name
    username: your-mysql-username
    password: your-mysql-password
  data:
    redis:
      host: localhost
      port: 6379

cloud:
  aws:
    credentials:
      access-key: your-aws-access-key
      secret-key: your-aws-secret-key
    s3:
      bucket: your-s3-bucket-name
    region:
      static: your-aws-region

openai:
  api-key: your-openai-api-key
📝 your- 로 시작하는 부분은 실제 값으로 교체하세요.



3️⃣ 프로젝트 실행 (Run)

./gradlew bootRun
프로젝트 루트 경로에서 명령어를 실행하세요.
```


## 📝 API 명세 (API Specification)
```bash
🐾 반려동물 (Pet)
Method	Endpoint	Description
POST	/api/pets	반려동물 등록
GET	/api/pets	반려동물 전체 목록 조회
GET	/api/pets/{id}	특정 반려동물 조회
PUT	/api/pets/{id}	반려동물 정보 수정
DELETE	/api/pets/{id}	반려동물 삭제
```


## 📊 건강 관리 (Health)
```bash
Method	Endpoint	Description
POST	/api/pets/{petId}/weights	체중 기록
GET	/api/pets/{petId}/weights	체중 기록 조회
POST	/api/pets/{petId}/health-notes	건강 수첩 작성
GET	/api/pets/{petId}/health-notes	건강 수첩 조회
GET	/api/pets/{petId}/health-reports	건강 리포트 조회
```


## 💉 예방 접종 (Vaccine)
```bash
Method	Endpoint	Description
POST	/api/pets/{petId}/vaccines	예방접종 기록 추가
GET	/api/pets/{petId}/vaccines	예방접종 기록 조회
PUT	/api/vaccines/{vaccineId}	예방접종 기록 수정
DELETE	/api/vaccines/{vaccineId}	예방접종 기록 삭제
```


## 🎯 미션 (Mission)
```bash
Method	Endpoint	Description
GET	/api/missions/daily	오늘의 데일리 미션 조회
POST	/api/missions/{missionId}/complete	미션 완료 기록
```


## 🤖 AI 챗봇 (AI Chatbot)
```bash
Method	Endpoint	Description
POST	/api/ai/question	AI 챗봇 질문/응답
```


## ☁️ 파일 (File)
```bash
Method	Endpoint	Description
POST	/api/files/upload	파일 업로드 (S3)
```


## 📁 프로젝트 구조 (Project Structure)
```bash
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/petlifecycle
│   │   │       ├── ai              # AI 챗봇
│   │   │       ├── auth            # 인증 및 권한 관리
│   │   │       ├── breed           # 품종 정보
│   │   │       ├── cycle           # 생애주기 관리
│   │   │       ├── global          # 전역 설정 및 예외 처리
│   │   │       ├── health          # 건강 기록 및 리포트
│   │   │       ├── metadata        # 파일 메타데이터 및 S3 연동
│   │   │       ├── mission         # 데일리 미션
│   │   │       ├── pet             # 반려동물 계정 정보
│   │   │       ├── redis_cache     # Redis 캐시
│   │   │       └── vaccine         # 예방접종 관리
│   │   └── resources
│   │       └── application.yaml    # 환경 설정 파일
│   └── test                        # 테스트 코드
└── build.gradle                    # 의존성 및 빌드 설정
```
