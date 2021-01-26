package pl.edu.pjwstk.jaz.allezon.configure;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Configuration
@Transactional
public class DataInitLoader implements ApplicationRunner {

    private final EntityManager entityManager;
    private final PasswordEncoder encoder;

    public DataInitLoader(EntityManager entityManager, PasswordEncoder encoder) {
        this.entityManager = entityManager;
        this.encoder = encoder;
    }

    public void run(ApplicationArguments args) {
        UserEntity user = new UserEntity();
        user.setRole_id(1);
        user.setEmail("admin");
        user.setPassword(encoder.encode("admin"));
        findOrCreate(user);
    }

    private void findOrCreate(UserEntity userEntity) {
        if (find(userEntity) == null) {
            create(userEntity);
        }
    }

    private UserEntity find(UserEntity userEntity) {
        try {
            UserEntity user = entityManager
                    .createQuery("select ue from UserEntity ue where ue.email=:email", UserEntity.class)
                    .setParameter("email", userEntity.getEmail())
                    .getSingleResult();
            return user;
        } catch (NoResultException msg) {
            return null;
        }
    }

    private void create(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }
}