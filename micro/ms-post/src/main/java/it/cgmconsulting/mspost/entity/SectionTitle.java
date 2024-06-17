package it.cgmconsulting.mspost.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SectionTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private byte id;

    @Column(nullable = false, length = 50, unique = true)
    private String sectionTitle;

    boolean visible = true;

    public SectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
}
