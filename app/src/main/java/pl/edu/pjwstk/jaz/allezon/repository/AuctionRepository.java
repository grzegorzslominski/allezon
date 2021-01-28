package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {

    AuctionEntity findByTitle(String title);

    List<AuctionEntity> findAllBySubcategoryId(Long subcategoryId);

}