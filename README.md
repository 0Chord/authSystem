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
|JWT|AccessToken, RefreshToken 발급<br>AccessToken, RefreshToken 검증<br>AccessToken 정보 추출|
|메일|회원가입 시 이메일 검증<br>비밀번호 찾기 시 임시비밀번호 발급|
|회원기입|비밀번호 암호화<br>가입된 회원인지 검증|
|로그인|회원 검증<br>JWT 발급<br>ROLE 구분<br>비밀번호 검증|
## 구현
#### 1. 회원가입
- 비밀번호 암호화
~~~Java
 @Override
    public String encrypt(String rawPassword){
        String[] rawPasswordList = rawPassword.split("");
        StringBuilder encodingBinaryPassword = new StringBuilder();
        StringBuilder encodingPassword = new StringBuilder();
        int idx;
        for (idx = 0; idx < rawPasswordList.length; idx++) {
            String binaryString = Integer.toBinaryString((int) rawPasswordList[idx].charAt(0) + idx);
            String paddingBinaryCode = String.format("%08d", Integer.parseInt(binaryString));
            encodingBinaryPassword.append(paddingBinaryCode);
        }
        for (idx = 0; idx < encodingBinaryPassword.length(); idx += 6) {
            String parsingString;
            if (idx + 6 < encodingBinaryPassword.length()) {
                parsingString = encodingBinaryPassword.substring(idx, idx + 6);
            } else {
                parsingString = encodingBinaryPassword.substring(idx);
            }
            int encodingNumber = Integer.parseInt((Integer.valueOf(parsingString, 2)).toString());
            encodingPassword.append(BASE64.charAt(encodingNumber));
        }
        return encodingPassword.toString();
    }
~~~
- 직접 암호화 알고리즘 구현
2. 메일 인증
 <img width="1102" alt="image" src="https://user-images.githubusercontent.com/114129008/209463852-2e789d18-1ea0-42e0-9eba-d6342ac36b15.png">

3. 비밀번호 변경
<img width="956" alt="image" src="https://user-images.githubusercontent.com/114129008/209463865-b8c73400-548e-4a02-ab5f-a2971fc4baf3.png">

4. 캐시 등록
``` Java
@Getter
@RequiredArgsConstructor
public enum CacheType {

    REFRESH_TOKEN_ID("refreshTokenId",24*60*60,10000),
    REFRESH_TOKEN("refreshToken",24*60*60,10000),
    USER_ID("userId",24*60*60,10000),
    Member("member",24*60*60,10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
```
- Caffeine Cache를 이용하여 DB접근 최소화
5. 로그인 시 JWT 발급
``` Java
@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MultiValueMap<String, String> body) throws URISyntaxException {
        String password = body.get("password").get(0);
        String userId = body.get("userId").get(0);
        Member member = memberService.findById(userId);
        if (member == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            if (bcrypt.matching(password, member.getPassword())) {
                TokenInfo tokenInfo = memberService.login(userId, password);
                jwtService.login(tokenInfo);
                return new ResponseEntity<>(tokenInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
```
<img width="706" alt="image" src="https://user-images.githubusercontent.com/114129008/209464023-61f81240-c561-4fa5-b6ab-cd65e376926a.png">

## 코드 중 확인받고 싶은 부분
```Java
public interface ApiRepository {
    APIKey save(APIKey apiKey);
    Optional<APIKey> findByDomain(String domain);
}

public class ApiJpaRepository implements ApiRepository{
    EntityManager em;

    public ApiJpaRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public APIKey save(APIKey apiKey) {
        em.persist(apiKey);
        return apiKey;
    }

    @Override
    public Optional<APIKey> findByDomain(String domain) {
        List<APIKey> result = em.createQuery("select a from APIKey a where a.domain = :domain", APIKey.class)
                .setParameter("domain",domain)
                .getResultList();

        return result.stream().findAny();
    }
}
```
를 
```Java
public interface ApiRepository extends JpaRepository<APIKey, String> {
    APIKey save(APIKey apiKey);
    Optional<APIKey> findByDomain(String domain);
}
```
로 사용할 때 단점이 있는지 궁금합니다.

#### 이번에 규모가 작아 Caffeine Cache를 사용하여 프로젝트를 진행했는데 Redis는 어느정도 규모 이상일 때 사용하면 좋은지 궁금합니다.
#### 8080 서버와 8081서버가 API통신을 통한 MSA를 구성하고 있는데 MSA가 맞는지 궁금하고 이 구조를 고도화 하기 위해서 어떤 방식으로 진행하면 되는지 궁금합니다.
