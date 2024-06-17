package it.cgmconsulting.mscomment.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EditorialStaffCommentRequest {

    @Min(1)
    private int commentId;
    @NotBlank @Size(min = 1,max = 255)
    private String comment;
}
