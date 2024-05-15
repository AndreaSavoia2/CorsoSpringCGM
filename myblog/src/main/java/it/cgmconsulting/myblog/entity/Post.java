package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Post extends CreationUpdate {

    @Transient
    @Value("${application.image.post.path}")
    private String path;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String title;

    private String overview;

    @Column(nullable = false, length = 65535 /*columnDefinition = "TEXT"*/)
    private String content;

    @Column(length = 16)
    private String image;

    private LocalDate publicationDate;

    private short totComments;

    @ManyToOne
    @JoinColumn(nullable = false, name="user_id")
    private User userId;

    @ManyToMany
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OrderBy("tagName ASC")
    public Set<Tag> tags = new HashSet<>();

    public Post(String title, String content, User userId, String overview) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.overview = overview;
    }

    // questi metodi servono per sincronizzare gli oggetti post e tag quando
    // aggiungo e rimuovo un tag ad un post.
    public void addTag(Tag tag){
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

}
