package it.cgmconsulting.mscomment.payload.response;

import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.utils.Consts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class EditorialStaffCommentResponse {

    private int escId;
    private String comment;
    private LocalDateTime createdAt;
    private String escAuthor;

    public EditorialStaffCommentResponse(int escId, String comment, LocalDateTime createdAt) {
        this.escId = escId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.escAuthor = Consts.REDAZIONE;
    }

    public static EditorialStaffCommentResponse fromEntityToResponse(EditorialStaffComment e){
        return new EditorialStaffCommentResponse(
                e.getId().getCommentId().getId(),
                e.getComment(),
                LocalDateTime.now()
        );
    }
}
