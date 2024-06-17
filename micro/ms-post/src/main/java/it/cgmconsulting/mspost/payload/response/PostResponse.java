package it.cgmconsulting.mspost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class PostResponse {

    private int id;
    private String title;
    private LocalDate publicationDate;
    private String author;

    public PostResponse(int id, String title, LocalDate publicationDate) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
    }
}
