package pl.edu.pjwstk.jaz.allezon.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "\"auction\"")
public class AuctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "subcategory_id")
    private Long subcategoryId;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Float price;
}