package it.cgmconsulting.mspost.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SectionTitle title;

    private String subTitle;

    @Column(nullable = false,length = 5000)
    private String content;

    private String sectionImage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int author;

    public Section(Post post,
                   SectionTitle title,
                   String subTitle,
                   String content,
                   String sectionImage,
                   int author) {
        this.post = post;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.sectionImage = sectionImage;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
}
