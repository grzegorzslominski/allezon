package pl.edu.pjwstk.jaz.allezon.entity;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "\"role\"")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "role")
    private String role;

    public Integer getId() {
        return id;
    }

    public Set<String> getRole() {
        return Collections.singleton(role);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
}