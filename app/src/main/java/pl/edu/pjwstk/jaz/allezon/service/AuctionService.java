package pl.edu.pjwstk.jaz.allezon.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionAndPhoto;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionImageRepository;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;

import java.util.List;

@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final List<AuctionAndPhoto> auctionAndPhotosList;


    public  void changeSubcategoriesToUndefined(Long subcategoryId){
        List<AuctionEntity> auctionEntityList= auctionRepository.findAllBySubcategoryId(subcategoryId);
        for (int i = 0; i < auctionEntityList.size(); i++) {
            auctionEntityList.get(i).setSubcategoryId(subcategoryRepository.findByName("Undefined").get().getId());
        }
    }

    public List <AuctionAndPhoto> displayAuctionsAndFirstPhoto() {

        List<AuctionEntity> auctionList = auctionRepository.findAll();
        for (int i=1; i<auctionList.size(); i++){
            AuctionAndPhoto auctionAndPhoto = new AuctionAndPhoto();
            auctionAndPhoto.setAuction(auctionList.get(i));
            if(auctionImageRepository.findFirstByAuctionId(auctionList.get(i).getId())!=null)
                auctionAndPhoto.setFirstPhoto(auctionImageRepository.findFirstByAuctionId(auctionList.get(i).getId()).getUrl());
            else {
                auctionAndPhoto.setFirstPhoto("default.photo/5.com");
            }
                auctionAndPhotosList.add(i,auctionAndPhoto);
        }
        return auctionAndPhotosList;
    }



}

