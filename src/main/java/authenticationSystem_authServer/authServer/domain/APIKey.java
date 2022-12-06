package authenticationSystem_authServer.authServer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "api")
public class APIKey {
    @Id
    private String domain;
    @Column(name="api_key")
    private String API;

}
