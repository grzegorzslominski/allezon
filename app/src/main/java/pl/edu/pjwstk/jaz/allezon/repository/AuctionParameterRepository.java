package pl.edu.pjwstk.jaz.allezon.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionParameterEntity;

import java.util.List;

@Repository
public interface AuctionParameterRepository extends JpaRepository<AuctionParameterEntity,Long> {

    List<AuctionParameterEntity> findAllByParameterId(Long parameterId);
}
