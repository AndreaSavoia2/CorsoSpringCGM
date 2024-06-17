package it.cgmconsulting.mspost.payload.response;

import it.cgmconsulting.mspost.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SectionResponse {

    private int sectionId;
    private int postId;
    private String sectionTitle;
    private String subTitle;
    private String content;
    private String sectionImage;

    public static SectionResponse mapToResponse(Section s){
        return new SectionResponse(
                s.getId(),
                s.getPost().getId(),
                s.getTitle().getSectionTitle(),
                s.getSubTitle(),
                s.getContent(),
                s.getSectionImage()
        );
    }
}
