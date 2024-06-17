package it.cgmconsulting.mscomment.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CommentFullResponseBis {

    private int id;
    private String author;
    private String comment;
    private LocalDateTime date;

    private String escAuthor;
    private String escComment;
    private LocalDateTime escDate;

}
