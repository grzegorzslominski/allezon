package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;
import pl.edu.pjwstk.jaz.allezon.repository.UserRepository;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;
import pl.edu.pjwstk.jaz.allezon.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuctionController {
    private final  AuctionRepository auctionRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserSession userSession;


    @GetMapping("allezon/auctions")
    public ResponseEntity<List<AuctionEntity>> getAuctions() {
        return new ResponseEntity(auctionRepository.findAll(), HttpStatus.OK);
    }


    @PostMapping("allezon/auctions")
    public ResponseEntity<String> addAuction(@RequestBody AuctionEntity auction) {


        auction.setAuthorId(userSession.getUserId());
        auctionRepository.save(auction);
        return new ResponseEntity("Added auction", HttpStatus.CREATED);
    }



        @PatchMapping("allezon/auctions/edit/{auctionId}")
        public ResponseEntity<String> editAuction (@PathVariable Long auctionId, @RequestBody AuctionEntity editAuction) {
            if(auctionRepository.findById(auctionId).isPresent()) {
                AuctionEntity auction = auctionRepository.findById(auctionId).get();

                if (userSession.getUserId()==auction.getAuthorId()) {
                   if(editAuction.getCategoryId()!=null&&subcategoryRepository.findById(editAuction.getCategoryId()).isPresent())
                       auction.setCategoryId(editAuction.getCategoryId());
                   if(editAuction.getTitle()!=null)
                       auction.setTitle(editAuction.getTitle());
                   if(editAuction.getDescription()!=null)
                       auction.setDescription(editAuction.getDescription());
                   if(editAuction.getPrice()!=null)
                       auction.setPrice(editAuction.getPrice());

                    auctionRepository.save(auction);
                    return new ResponseEntity("The auction  has been changed", HttpStatus.ACCEPTED);
                }
                else {
                    return new ResponseEntity<>("Editing impossible, you are not the owner of the action", HttpStatus.UNAUTHORIZED);
                }
            }
            else  {
                return new ResponseEntity<>("The given auctions does not exist.", HttpStatus.NOT_FOUND);
            }
        }
}