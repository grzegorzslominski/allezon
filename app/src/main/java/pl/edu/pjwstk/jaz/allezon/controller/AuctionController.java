package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuctionController {
    private final AuctionRepository auctionRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserSession userSession;


    // lista wszystkich aukcji (tylko zalogowani)
    @GetMapping("allezon/auctions")
    public ResponseEntity<List<AuctionEntity>> getAuctions() {
        return new ResponseEntity(auctionRepository.findAll(), HttpStatus.OK);
    }

    //dodanie aukcji (tylko zalogowani)
    @PostMapping("allezon/auctions")
    public ResponseEntity<String> addAuction(@RequestBody AuctionEntity auction) {

        if (subcategoryRepository.findById(auction.getSubcategoryId()).isPresent()) {
            auction.setAuthorId(userSession.getUserId());
            auctionRepository.save(auction);
            return new ResponseEntity("Added auction", HttpStatus.CREATED);
        } else
            return new ResponseEntity("Given subcategory does not exist", HttpStatus.NOT_FOUND);
    }

    //usuwanie aukcji (tylko właściciel aukcji)
    @DeleteMapping("allezon/auctions/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable Long auctionId) {
        if (auctionRepository.findById(auctionId).isPresent()) {

            if (userSession.getUserId() == auctionRepository.findById(auctionId).get().getAuthorId()) {
                auctionRepository.delete(auctionRepository.findById(auctionId).get());
                return new ResponseEntity("The auction  has been deleted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Deleted impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("The given auctions does not exist.", HttpStatus.NOT_FOUND);
        }
    }


    //edycja aukcji (tylko właściciel aukcji)
    @PatchMapping("allezon/auctions/edit/{auctionId}")
    public ResponseEntity<String> editAuction(@PathVariable Long auctionId, @RequestBody AuctionEntity editAuction) {
        if (auctionRepository.findById(auctionId).isPresent()) {
            AuctionEntity auction = auctionRepository.findById(auctionId).get();

            if (userSession.getUserId() == auction.getAuthorId()) {
                if (editAuction.getSubcategoryId() != null && subcategoryRepository.findById(editAuction.getSubcategoryId()).isPresent())
                    auction.setSubcategoryId(editAuction.getSubcategoryId());
                if (editAuction.getTitle() != null)
                    auction.setTitle(editAuction.getTitle());
                if (editAuction.getDescription() != null)
                    auction.setDescription(editAuction.getDescription());
                if (editAuction.getPrice() != null)
                    auction.setPrice(editAuction.getPrice());

                auctionRepository.save(auction);
                return new ResponseEntity("The auction  has been changed", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Editing impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("The given auction does not exist.", HttpStatus.NOT_FOUND);
        }
    }
}