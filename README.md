# CTR(Coding Test Records)


## 프로젝트 개요

- 개인 프로젝트
- 프로젝트 명: Spring Boot 기반 개인 별 코딩 테스트 기록 사이트
- 기능:
  1. 최근 푼 문제 이력
  2. 특정 문제의 풀이 횟수, 풀이 시간, 풀이 상태 기록
  3. 각 풀이 이력 별 풀이 시간(풀이 시작, 종료 시각, 풀이 시간)
  4. 풀이 코드, 메모 저장 가능

## 프로젝트 기간

- 2025.12--2026.01 중순 초안 제작, 이후~ (진행 중)
- 기본적인 사이트 외형은 1달 소요, 더미 데이터 생성(약 1억), 사이트 최적화는 보름 정도 소요. 최적화는 현재 진행 중.
- 기능 추가, 확장 및 리팩토링 진행 중

## 개발 환경

- Language: Java 17
- Framework: Spring Boot 3.5, Spring Security
- Build Tool: Gradle
- DB: MySQL(Docker)
- Template: Thymeleaf
- 기타: JDBC

---
## 시스템 구조
<img width="914" height="597" alt="image" src="https://github.com/user-attachments/assets/f313e3d8-32bc-4858-9bc0-17ca560d2eaf" />

## Scheme
<img width="935" height="431" alt="image" src="https://github.com/user-attachments/assets/9e09a235-76ee-4c23-af92-37786f008988" />

| Table name | 역할 |
| --- | --- |
| user | 회원 정보 |
| my | 회원 별 풀이 이력 |
| problem | 문제 데이터 |
| tag | 문제 태그(알고리즘 등) |
| problem_tag | 문제-tag 매핑 |

## 주요 기능

- 유저 별 문제 풀이 데이터 Create/Read/Update
	→ 데이터 보존, 무결성을 위해 삭제는 없음, 잘못된 기록은 메모 필요
- 풀이 시간 기록, 코드, 메모 기록 가능
- MySQL 유저 데이터 기반 로그인
- `Redis Session` 기반 로그인 상태 유지
- 라즈베리파이 3B+ 구축했을 시에는 MySQL은 Docker 이식, Spring은 local PC에서 운영.
- 프로젝트가 늘어나 미니 PC를 사용하기 시작했을 때에는 모든 시스템을 미니 PC에 이식하여 Docker 이용하여 사용 중.

## 실행 및 테스트 방법

1. `Git clone` 후, `application.yml` 또는 `application.properties` 설정
2. DB 연동 (MySQL)
3. 로컬에서 실행: `./gradlew bootRun`
4. 브라우저 접속: `http://localhost:8080/`

## 추가 진행 사항

### 1. solved.ac API 데이터 추출
- 사이트 이용을 위해선 문제를 선택해야 하는데 그러기 위해선 문제 데이터가 필요.
- 방식이 몇 가지 있으나 solved.ac API 이용이 비공식 API지만 효율이 좋아 채택.
- 요청이 많으면 서버로부터 `429 Too many requests` 응답이 발생하여 적절한 API 항목 선택, 호출 간격 등 조절 필요.

### 2. 대규모 데이터 부하 테스트(1억)
- 이전 프로젝트인 `BoardProject`에서 매크로를 사용하여 더미 데이터를 생성한 것을 응용.
- 회원 별 풀이 이력 table `my`의 데이터가 누적됐을 때의 상황을 가정.
- 실제 기업과 유사한 환경을 만들고자 더미 데이터를 가능한 한 많이 넣기 위해 실험 진행.
- 코드 작성 후 숫자만 조절하여 데이터를 생성해보았는데 1000만까지 생성.
- 그 이상으로 생성 시 데이터 삽입 속도가 더뎌지는 것이 확인.
- DB의 FK Check로 인한 속도 저하 확인 후 삽입 시 일시적으로 Check 해제하는 것으로 설정 변경.
- 설정 변경 후 삽입하는 동안 동일 속도를 유지하나 1억 데이터 이상으론 저장 공간 부족의 우려로 용량 증대 중단.
- 이후 query 최적화, index 설계 등 다양한 과정을 거쳐 쿼리 응답 속도 **최대 26초→0.0x초** 도달 성공.

### 3. 도커 이식 실험
- 원래 세웠던 계획은, 기초 테스트는 완료하였으므로, 여타 과정과 마찬가지로 AWS EC2로 서버를 이전하여 배포할 예정이었음.
- 하지만 제 아무리 무료 플랜이 있어도 결국은 데이터를 전송하게 되면 비용이 발생할 우려가 발생.
- 학부, 대한상공회의소 수강 당시 사용한 라즈베리파이 3B+를 떠올림.
- 바로 옮기기 전에 도커의 장점으로 손꼽히던 컨테이너 이식성을 마이그레이션 연습 겸 테스트하기로 결정.
- 경우의 수
  1. 같은 컴퓨터 내 컨테이너 → 컨테이너 이식: 성공, 빠름, Volume 이식 가능
  2. 다른 컴퓨터 간(양쪽 모두 Windows 11) 컨테이너 이식: 성공, 빠름, Volume 이식 가능
  3. 컴퓨터(Windows 11) → 라즈베리파이(Linux, Raspberry Pi OS): 다수의 실패 후 성공, 별도의 설정 필요, 복잡, 시간 다수 소요, Volume 이식이 불가능에 가까움, `sql dump` 기능 필요.
  4. Linux 간 컨테이너 이식: 시도 안 해봄, 종류가 다양하여 설정의 필요성이 있을지 Windows처럼 이식이 쉬울지 미지수, 추후 진행 예정.

### 라즈베리파이 성능 최적화
- 대한상공회의소 수강 시 방열판, 쿨링팬 등 이미 장착하여 온도 문제는 없었음(Idle ±6도).
- `watch –n 1 "vcgencmd measure_temp && free –m && top"` 명령어를 사용하여 1초마다 온도, memory 사용량, CPU 점유율을 확인하면서 진행.
- Data insert 시 top에선 Disk IO 대기율(작업이 밀려있는 정도)인 WA를 확인, 라즈베리파이는 48%까지 기록.
- AI의 조언에 따라 (64GB SD카드를 사용하고 있었으므로) 2500MB를 Swap memory로 사용하여 작업.
- 별도로 작업 속도는 측정해보지 않았으나 memory를 늘린 만큼 작업 시 많은 memory를 점유하는 것을 확인.

### Redis Session 도입
- 웹 Layout 작업 시 Java 앱을 재실행하면 Session도 같이 초기화되어 매번 로그인을 해야 하는 번거로움이 발생.
- Session을 Spring 내부가 아닌, 별도의 서버에서 생성해야 로그인이 유지됨.
- Sping Guides나 Spring Session 등 여러 문서를 참고하여 작업 시도.
- 지금도 어렵다고 느끼기에 자세히는 적지 못하지만 브라우저의 JSESSION과 Spring의 SESSION도 맞추고 쿠키의 문제도 해결해야 했음.
- 이 과정에서 Spring Security 데이터의 직렬화 문제가 있었는데 이 부분이 제일 어려웠고 시작한 지 대략 6시간 후 해결하였음.
