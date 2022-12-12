package authenticationSystem_authServer.authServer.jwt;

import authenticationSystem_authServer.authServer.domain.RefreshToken;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    private String secretKey= "thisisjsonwebtokensecretkeythisisusedmyproject";
    private final Long tokenValidTime = 30*60*1000L;
    private final UserDetailsService userDetailsService;
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public TokenInfo createToken(String userId, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles",roles);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime*2*24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenInfo.builder().accessToken(accessToken).refreshToken(refreshToken).userId(userId).grantType("Bearer").build();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public String validateRefreshToken(RefreshToken refreshTokenObj){
        String refreshToken = refreshTokenObj.getRefreshToken();
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);

            if(!claims.getBody().getExpiration().before(new Date())){
                return recreationAccessToken(claims.getBody().get("sub").toString(),claims.getBody().get("roles"));
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public String recreationAccessToken(String userId, Object roles){
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles",roles);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }
}
