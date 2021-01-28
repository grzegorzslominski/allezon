package pl.edu.pjwstk.jaz.allezon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;

import java.util.List;


@AllArgsConstructor
@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final SubcategoryRepository subcategoryRepository;


    public  void changeSubcategoriesToUndefined(Long subcategoryId){
        List<AuctionEntity> auctionEntityList= auctionRepository.findAllBySubcategoryId(subcategoryId);
        for (int i = 0; i < auctionEntityList.size(); i++) {
            auctionEntityList.get(i).setSubcategoryId(subcategoryRepository.findByName("Undefined").get().getId());
        }
    }
}
