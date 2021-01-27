package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;


import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {


    Optional<CategoryEntity> deleteByName (String name);

    Optional<CategoryEntity> findByName(String name);
}
