package it.cgmconsulting.mspost.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SectionUpdateRequest {

    @Min(1)
    private byte sectionTitleId;
    @Size(max = 255)
    private String subTitle;
    @NotBlank @Size(max = 5000)
    private String content;
    @Size(max = 255)
    private String sectionImage;

}
