package pl.edu.pjwstk.jaz.allezon.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Setter
@Getter
@Entity
@Table(name = "auction_image")
public class AuctionImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column (name = "auction_id")
    private Long auctionId;

    @Column (name = "url")
    private String url;

}
