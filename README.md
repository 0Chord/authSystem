# 인증시스템 인증서버
- 웹서버는 https://github.com/0Chord/authenticationSystem 으로 분리
## 역할
|서비스|역할|
|:--:|--|
|인증서버|웹서버로부터의 모든 인증을 담당<br>웹서버로의 정보 제공하는 역할<br>DB를 통해 Entity 조회|
## 기술스택
- Java 17
- Spring Boot
- JWT
- JPA
- Caffeine Cache
- MySQL
- Mail
- Spring Security

## 아키텍처
<img width="758" alt="image" src="https://user-images.githubusercontent.com/114129008/209445519-9166d915-cca7-4d54-b1c6-4659736bffea.png">

## 제공 기능
|기능|설명|
|:--:|--|
|
