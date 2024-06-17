package it.cgmconsulting.mspost.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SectionRequest extends SectionUpdateRequest{

    @Min(1)
    private int postId;


}
