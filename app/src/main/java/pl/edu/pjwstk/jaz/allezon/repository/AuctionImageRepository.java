package pl.edu.pjwstk.jaz.allezon.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionImageEntity;

@Repository
public interface AuctionImageRepository extends JpaRepository<AuctionImageEntity,Long> {

    void deleteAllByAuctionId (Long auctionId);

    AuctionImageEntity findFirstByAuctionId(Long auctionId);
}
