package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.UserDTO;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;
import pl.edu.pjwstk.jaz.allezon.security.AuthenticationToken;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Repository
public class UserRepository {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;

    public UserRepository(EntityManager entityManager, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    public void saveUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        Integer userRoleId = 2;
        userEntity.setRole_id(userRoleId);
        entityManager.persist(userEntity);
    }

    public boolean emailExists(String email) {
        try {
            entityManager.createQuery("select ue from UserEntity ue where ue.email=:email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return true;
        } catch (NoResultException msg) {
            return false;
        }
    }

    public UserEntity findByEmail(String email) {
        try {
            UserEntity userEntity = entityManager.createQuery("select ue from UserEntity ue where ue.email=:email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return userEntity;
        } catch (NoResultException msg) {
            return null;
        }
    }

    public boolean login(UserDTO userDTO) {
        UserEntity userEntity = findByEmail(userDTO.getEmail());
        if (userEntity != null) {
            if (passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword())) {
                userSession.logIn();
                SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(userEntity, entityManager));
                return true;
            }
        }
        return false;
    }
}
