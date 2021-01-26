package pl.edu.pjwstk.jaz.allezon.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.pjwstk.jaz.allezon.entity.RoleEntity;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticationToken extends AbstractAuthenticationToken {
    public AuthenticationToken(UserEntity userEntity, EntityManager entityManager) {
        super(toGrantedAuthorities(entityManager.createQuery("select re from UserEntity ue, RoleEntity re where ue.email=:email and re.id=:role_id", RoleEntity.class)
                .setParameter("email", userEntity.getEmail())
                .setParameter("role_id", userEntity.getRole_id())
                .getSingleResult().getRole()));
        setAuthenticated(true);
    }

    private static Collection<? extends GrantedAuthority> toGrantedAuthorities(Set<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
