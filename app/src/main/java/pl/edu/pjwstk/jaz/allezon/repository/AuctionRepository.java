package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {

    AuctionEntity findByTitle(String title);

}