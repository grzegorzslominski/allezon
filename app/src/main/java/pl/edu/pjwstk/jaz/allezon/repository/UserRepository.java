package pl.edu.pjwstk.jaz.allezon.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;


import javax.transaction.Transactional;
import java.util.Optional;



@Transactional
@Repository

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional <UserEntity> findByEmail(String email);



}
