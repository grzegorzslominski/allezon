package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Long> {

    Optional<SubcategoryEntity> findByName(String name);

}
