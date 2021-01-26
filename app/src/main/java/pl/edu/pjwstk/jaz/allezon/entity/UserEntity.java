package pl.edu.pjwstk.jaz.allezon.entity;

import javax.persistence.*;

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

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
