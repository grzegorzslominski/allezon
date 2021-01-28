package pl.edu.pjwstk.jaz.allezon.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@RequiredArgsConstructor
@Component
public class AuctionAndPhoto {
    private AuctionEntity auction;
    private String firstPhoto ;


}
