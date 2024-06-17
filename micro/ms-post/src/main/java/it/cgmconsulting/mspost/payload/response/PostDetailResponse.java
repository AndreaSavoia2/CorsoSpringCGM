package it.cgmconsulting.mspost.payload.response;

import it.cgmconsulting.mspost.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor
public class PostDetailResponse {

    // form ms-post (post)
    private int id;
    private String title;
    private LocalDate publicationDate;
    private String postImage;

    // from ms-post (section)
    Set<SectionResponse> sections = new HashSet<>();

    // from ms-auth (user)
    private String author;

    // from ms-tag
    private Set<String> tagNames;

    private double average;

    public PostDetailResponse(int id, String title, LocalDate publicationDate, String postImage) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.postImage = postImage;
    }

    public PostDetailResponse(int id, String title, LocalDate publicationDate, String postImage, String author) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.postImage = postImage;
        this.author = author;
    }

}
