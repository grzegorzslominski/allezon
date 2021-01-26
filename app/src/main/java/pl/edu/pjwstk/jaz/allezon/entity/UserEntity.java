package pl.edu.pjwstk.jaz.allezon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "role_id")
    private Integer role_id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    public String getEmail() {
        return email;
    }
}
