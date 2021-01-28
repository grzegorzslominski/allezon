package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.ParameterEntity;

import java.util.List;


@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity,Long> {

    ParameterEntity findByName (String name);


    List<ParameterEntity> findAllById(Long parameterId);
}
