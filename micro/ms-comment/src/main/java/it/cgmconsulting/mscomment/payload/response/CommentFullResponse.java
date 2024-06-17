package it.cgmconsulting.mscomment.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CommentFullResponse {

    CommentResponse comment;
    EditorialStaffCommentResponse escComment;
}
