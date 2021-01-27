package pl.edu.pjwstk.jaz.allezon.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;
import pl.edu.pjwstk.jaz.allezon.repository.UserRepository;
import pl.edu.pjwstk.jaz.allezon.security.AuthenticationToken;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

import javax.persistence.EntityManager;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final UserSession userSession;
private final EntityManager entityManager;


    public void saveUser(UserEntity user) {
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole_id(2);
        userRepository.save(user);
    }

    public boolean login(UserEntity user) {
        Optional  userEntity =  userRepository.findByEmail(user.getEmail());
        if (userEntity.isPresent()) {
            UserEntity userEntity1 = (UserEntity) userEntity.get();
            if (passwordEncoder.matches(user.getPassword(), userEntity1.getPassword())) {
                userSession.logIn();
                userSession.setUserId(((UserEntity) userEntity.get()).getId());
                SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(userEntity1, entityManager));
                return true;
            }
        }
        return false;
    }
}
