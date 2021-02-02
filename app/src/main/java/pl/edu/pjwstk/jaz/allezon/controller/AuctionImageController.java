package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionImageEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionImageRepository;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

@RequiredArgsConstructor
@RestController
public class AuctionImageController {

    private final AuctionRepository auctionRepository;
    private final UserSession userSession;
    private final AuctionImageRepository auctionImageRepository;

    //dodanie zadjęcia do aukcji (tylko właściciel aukcji)
    @PostMapping("allezon/auctions/images/{auctionId}")
    public ResponseEntity<String> addAuctionParameter(@PathVariable Long auctionId, @RequestBody AuctionImageEntity imageAuction)
    {
        if(auctionRepository.findById(auctionId).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionId).get().getAuthorId()) {
                auctionImageRepository.save(imageAuction);
                return new ResponseEntity("Photo has been added", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Adding an photo impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given auctions does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    //usuwanie zdjęcia z aukcji (tylko właściciel)
    @DeleteMapping ("allezon/auctions/images/{imageId}")
    public ResponseEntity<String> deleteAuctionImage(@PathVariable Long imageId)
    {
        if(auctionImageRepository.findById(imageId).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionImageRepository.findById(imageId).get().getAuctionId()).get().getAuthorId()) {
                auctionImageRepository.deleteById(imageId);
                return new ResponseEntity("Photo has been deleted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Deleted an photo impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given photo does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    //edycje zdjęcia z aukcji (tylko właściciel)
    @PatchMapping ("allezon/auctions/images/edit/{imageId}")
    public ResponseEntity<String> editAuctionImage(@PathVariable Long imageId,@RequestBody AuctionImageEntity auctionImageEntity){
        if(auctionImageRepository.findById(imageId).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionImageRepository.findById(imageId).get().getAuctionId()).get().getAuthorId()) {
                AuctionImageEntity imageEntity = auctionImageRepository.findById(imageId).get();
                if(auctionImageEntity.getUrl()!=null){
                    imageEntity.setUrl(auctionImageEntity.getUrl());
                }
                auctionImageRepository.save(imageEntity);
                return new ResponseEntity("Photo has been edited", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Edited an photo impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given auctions does not exist.", HttpStatus.NOT_FOUND);
        }

    }
}
