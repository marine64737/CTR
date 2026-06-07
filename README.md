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

## 트러블 슈팅
<details>
<summary>회원 가입</summary>
<div markdown="1">
	기존 Query - name: MULTI key
	<br>
	<img width="650" height="212" alt="image" src="https://github.com/user-attachments/assets/a7444525-749d-4ee5-ad83-278f7a681974" />
	<img width="1447" height="667" alt="image" src="https://github.com/user-attachments/assets/087aa8a1-cee3-4b2f-952c-9a884f50b19a" />
	수정된 Query - name: UNIQUE key
	<br>
	<img width="620" height="185" alt="image" src="https://github.com/user-attachments/assets/60090968-79c6-460e-af63-23efe968cc40" />
	<img width="1314" height="697" alt="image" src="https://github.com/user-attachments/assets/8282c614-c1aa-400a-b69a-7b37a80bb694" />
	name이 unique key가 되어 중복 가입 불가, Query에서도 기존 로직은 찰나의 순간 가입 버튼이 두 번 눌리거나 동일한 이름의 사람이 우연히 동시에 가입할 때 중복으로 등록이 가능했다면, 수정된 로직은 같은 name으로 name이 존재하지 않을 경우에 `insert`가 이루어지고, 찰나의 순간이어도 db에 등록되어 name이 존재하면 0을 출력하여 아무 일도 발생하지 않는다.
</div>
</details>
<details>
<summary>메인 화면</summary>
<div markdown="1">
	기존 Query<br>유저 별 풀었던 문제를 문제 번호 순으로 Group화하여 문제 별로 몇 번 풀었는지 출력<br>
	<img width="1500" height="130" alt="image" src="https://github.com/user-attachments/assets/059e752a-4761-4024-9060-00785f044154" />
	수정된 Query<br>유저 별 풀었던 문제를 최근에 푼 순으로 출력<br>
	<img width="1500" height="143" alt="image" src="https://github.com/user-attachments/assets/4e46cccc-88d5-45fc-b047-10c6ffb2aaf6" />
	유저 입장에서는 같은 문제를 얼마나 많이 풀었는지는 중요하지 않을 것이라 예상.<br>
	‘내’가 최근에 무슨 문제를 풀었는지 복기하는 것이 더 필요할 것이라는 판단.<br>
	Subquery 추가한 Query - Subquery로 순서대로 조건을 불러오는 방식으로 변경<br>
	<img width="1500" height="378" alt="image" src="https://github.com/user-attachments/assets/23d8e66d-e419-408a-85ff-d9c61f8db585" />
	Subquery 도입만으로도 ‘수정된 Query’보다 더 빠른 응답 속도를 보임. (약 0.6초 -> 0.2초) 첫 응답 시 0.2초며, 캐시가 저장되면 0.0x초 출력됨.
	현재 Query - 최종 Query로, 코딩 테스트를 진행하면서 불편한 부분에 대해서 수정한 부분들.
	
	```java
	//my: 현재까지 푼 문제(start_time이 적혀있는 문제)
        List<Map<String, Object>> my = jdbcTemplate.queryForList("SELECT m.problemid as pid, p.titleKo as title, m.status, p.level " +
                        "FROM (SELECT id, userid, problemid, status FROM my WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)" +
                        "    AND start_time IS NOT NULL ORDER BY start_time DESC LIMIT 100) AS m " +
                        "JOIN user u ON m.userid = u.id JOIN problem p ON m.problemid = p.problemId;",
                principal.getName());
	//my1: 추가는 했지만 아직 풀지 않고 대기 중인 문제(To-do list( (start_time이 null인 문제)
        List<Map<String, Object>> my1 = jdbcTemplate.queryForList("SELECT m.id as id, m.problemid as pid, p.titleKo as title, m.status, m.nonvisible " +
                        "FROM (SELECT id, userid, problemid, status, nonvisible FROM my " +
                        "    WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)" +
                        "    AND start_time IS NULL ORDER BY id DESC LIMIT 100) AS m " +
                        "JOIN user u ON m.userid = u.id JOIN problem p ON m.problemid = p.problemId;",
                principal.getName());
	//probNum: 현재까지 푼 문제 수(중복 없이)
        Object probNum = jdbcTemplate.queryForObject("SELECT count(distinct problemid) as count FROM my " +
                        "    WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)" +
                        "    AND start_time IS NOT NULL",
                (rs, rowNum) -> rs.getInt("count"), principal.getName());
        if (probNum != null) model.addAttribute("count", Integer.parseInt(probNum.toString())); // 메인 페이지 상위에 문제 수 출력
	```
	
</div>
</details>
<details>
<summary>문제 검색</summary>
<div markdown="1">
	기존 Query - 단순 검색 결과 출력으로, 단점으로는 풀고 있는지, 이미 풀었던 문제인지를 따지지 않고 풀이 이력을 중복으로 추가할 우려가 있음.<br>
	<img width="1500" height="60" alt="image" src="https://github.com/user-attachments/assets/a102956c-0420-413d-aad6-4cafe2a39115" />
	현재 Query - 최종 Query로, 코딩 테스트를 진행하면서 불편한 부분에 대해서 수정한 부분들.
	
	```java
        String sql = "%"+word+"%";
        String name = principal.getName();
        List<Map<String, Object>> problems = jdbcTemplate.queryForList("select p.problemid, p.titleKo, m.userid from problem as p left join (SELECT distinct problemId, userid FROM my WHERE userid\n" +
                        "= (SELECT id FROM user WHERE name = ? LIMIT 1)) as m on p.problemid = m.problemid where p.problemId LIKE ?\n" +
                        "or p.titleKo LIKE ?;",
                name, sql, sql);
	```

	left join한 상태인데, userid를 확인하여 null인 경우(푼 적이 없는 경우) 추가, null이 아닌 경우(푼 적 있는 경우) 링크 이동으로 구분하여 문제 풀이를 진행할 수 있음.
</div>
</details>
<details>
<summary>선택한 문제의 풀이 이력</summary>
<div markdown="1">
	기존 Query<br>
	<img width="1500" height="169" alt="image" src="https://github.com/user-attachments/assets/6dda822b-1977-48e0-b5ef-08e82dc49b25" />
	최적화 Query<br>
	<img width="1500" height="393" alt="image" src="https://github.com/user-attachments/assets/508be07c-dbd7-4c32-b0da-adfc98aac140" />
	현재 Query - column이 추가된 것 외에는 큰 차이 없음.
	
	```java
        int pid = Integer.parseInt(problemid);
        String name = principal.getName();
        List<Map<String, Object>> my = jdbcTemplate.queryForList("SELECT m.id as id, m.problemid as pid, " +
                        "DATE_FORMAT(m.start_time, '%Y-%m-%d %H:%i:%s') as st, DATE_FORMAT(m.end_time, '%Y-%m-%d %H:%i:%s') as end, "+
                        "TIMESTAMPDIFF(MINUTE, start_time, end_time) as duration, " +
                        "TIMESTAMPDIFF(HOUR, start_time, end_time) as hour, m.status, m.memory, m.time "+
                        "FROM (" +
                        "    SELECT id, userid, problemid, start_time, end_time, status, memory, time " +
                        "    FROM my " +
                        "    WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1) " +
                        "    and problemid = ? " +
                        ") AS m " +
                        "JOIN user u ON m.userid = u.id JOIN problem p on m.problemid = p.problemid ORDER BY start_time IS NULL DESC, start_time DESC",
                name, pid);
        String title = jdbcTemplate.queryForObject("SELECT titleKo from problem where problemid = ?",
                (rs, rowNum) -> rs.getString("titleKo"), pid);
        Object status = jdbcTemplate.queryForObject("SELECT status from my where userid = (select id from user where name = ?) and problemid = ? order by id desc limit 1", (rs, rowNum) -> rs.getInt("status"), name, pid);
        int i_status=0;
        if (status != null) i_status = Integer.parseInt(status.toString());
	```
	
	<br>
	pid는 pathvariable로 뺀 건데 int parsing 안 하고도 사용 가능한지는 확인 필요. my list는 start_time이 null인 항목이 맨 위에 올라와야 최신 순으로 보기 편하기 때문에 추가했고 start_time desc는 과거에 푼 이력을 최신 순으로 나열한 것.<br>
	status는 마지막 문제 풀이 상태가 미해결, 혹은 풀이 완료 상태일 때만 '재시도' 버튼이 떠서 이력을 추가하고자 할 때만 누를 수 있게 설정.
</div>
</details>
<details>
<summary>풀이 기록하기</summary>
<div markdown="1">
	특별한 내용은 없다. 저장하는 기능(update)으로 끝. 유일하게 css를 별도로 넣은 부분이다.<br>
	
	```java
    	jdbcTemplate.update("update my set code=?, memo=?, memory=?, time=? where id=?", code, memo, memory, time, mid);
	```
	
</div>
</details>

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
- 이후 query 최적화, index 설계 등 다양한 과정을 거쳐 쿼리 응답 속도 **최대 25.946초→0.0x초** 도달 성공.

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
