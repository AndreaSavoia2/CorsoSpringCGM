package it.cgmconsulting.mspost.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    Set<Section> sections = new HashSet<>();

    @Column(length = 255)
    private String postImage;

    private int author;

    private LocalDate publicationDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Post(String title, String postImage, int author) {
        this.title = title;
        this.postImage = postImage;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public Post addSection(Section s){
        sections.add(s);
        s.setPost(this);
        return this;
    }

    public Post removeSection(Section s){
        sections.remove(s);
        return this;
    }
}
