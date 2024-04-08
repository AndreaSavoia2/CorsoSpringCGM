package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private short id;

    @Column(length = 50, nullable = false, unique = true)
    private String tagName;

    private  boolean visible = true; // in db 0 = false , 1 = true

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
