package pl.edu.pjwstk.jaz.allezon.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Setter
@Getter
@Entity
@Table(name = "auction_parameter")
public class AuctionParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column (name = "auction_id")
    private Long auctionId;

    @Column (name = "parameter_id")
    private Long parameterId;

    @Column (name = "value")
    private String value;
}

