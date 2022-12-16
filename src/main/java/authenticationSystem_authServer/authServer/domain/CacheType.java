package authenticationSystem_authServer.authServer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    REFRESH_TOKEN_ID("refreshTokenId",24*60*60,10000),
    REFRESH_TOKEN("refreshToken",24*60*60,10000),
    USER_ID("userId",24*60*60,10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
