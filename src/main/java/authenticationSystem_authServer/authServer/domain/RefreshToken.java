package authenticationSystem_authServer.authServer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="refresh_token")
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "user_id")
    private String userId;

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}
}
