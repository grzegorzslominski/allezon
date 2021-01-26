package pl.edu.pjwstk.jaz.allezon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"role\"")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "role")
    private String role;



    public Set<String> getRole() {
        return Collections.singleton(role);
    }

}