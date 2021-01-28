package pl.edu.pjwstk.jaz.allezon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionParameterEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionParameterRepository;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionRepository;
import pl.edu.pjwstk.jaz.allezon.repository.ParameterRepository;
import pl.edu.pjwstk.jaz.allezon.security.UserSession;

@RequiredArgsConstructor
@RestController
public class AuctionParameterController {

    private final AuctionRepository auctionRepository;
    private final UserSession userSession;
    private final ParameterRepository parameterRepository;
    private final AuctionParameterRepository auctionParameterRepository;



    //dodanie parametru aukcji (tylko właściciel aukcji)
    @PostMapping("allezon/auctions/parameter/{auctionId}")
            public ResponseEntity<String> addAuctionParameter(@PathVariable Long auctionId, @RequestBody AuctionParameterEntity parameterAuction)
    {
        if(auctionRepository.findById(auctionId).isPresent()&&parameterRepository.findById(parameterAuction.getParameterId()).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionId).get().getAuthorId()) {
                auctionParameterRepository.save(parameterAuction);
                return new ResponseEntity("Auction parameter has been added", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Adding an auction parameter impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given auctions or parameter does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    //usuwanie parametru aukcji (tylko właściciel aukcji)
    @DeleteMapping("allezon/auctions/parameter/{parameterId}")
    public ResponseEntity<String> addAuctionParameter(@PathVariable Long parameterId)
    {
        if(auctionParameterRepository.findById(parameterId).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionParameterRepository.findById(parameterId).get().getAuctionId()).get().getAuthorId()) {
                auctionParameterRepository.deleteById(parameterId);
                return new ResponseEntity("Auction parameter has been deleted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Deleted an auction parameter impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given auction parameter does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    //edycja parametru aukcjii (tlyko właściciel)
    @PatchMapping("allezon/auctions/parameter/edit/{parameterId}")
    public ResponseEntity<String> editAuctionParameter(@PathVariable Long parameterId,@RequestBody AuctionParameterEntity auctionParameterEntity)
    {
        if(auctionParameterRepository.findById(parameterId).isPresent()) {
            if (userSession.getUserId() == auctionRepository.findById(auctionParameterRepository.findById(parameterId).get().getAuctionId()).get().getAuthorId()) {
                AuctionParameterEntity auctionParameter = auctionParameterRepository.findById(parameterId).get();
                if(auctionParameterEntity.getParameterId()!=null) {
                    auctionParameter.setParameterId(auctionParameterEntity.getParameterId());
                }
                if(auctionParameterEntity.getValue()!=null){
                    auctionParameter.setValue(auctionParameterEntity.getValue());
                }
                auctionParameterRepository.save(auctionParameter);
                return new ResponseEntity("Auction parameter has been edited", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("edited an auction parameter impossible, you are not the owner of the auction", HttpStatus.UNAUTHORIZED);
            }
        }
        else  {
            return new ResponseEntity<>("The given auction parameter does not exist.", HttpStatus.NOT_FOUND);
        }
    }
}
