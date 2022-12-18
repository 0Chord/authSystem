package authenticationSystem_authServer.authServer.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="mail_auth")
public class MailAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="member_id")
    private String memberId;

    private String code;
}
