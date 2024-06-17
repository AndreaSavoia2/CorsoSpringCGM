package it.cgmconsulting.mscomment.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String comment;

    private int author;
    private int post;

    private boolean censored = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Comment(String comment, int author, int post) {
        this.comment = comment;
        this.author = author;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }
}