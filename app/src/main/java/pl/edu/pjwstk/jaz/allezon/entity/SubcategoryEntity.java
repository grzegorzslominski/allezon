package pl.edu.pjwstk.jaz.allezon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@ToString
@Setter
@Getter
@Entity
@Table(name = "subcategory")
public class SubcategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "category_id")
    private Long categoryId;



}
