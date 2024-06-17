package it.cgmconsulting.mscomment.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EditorialStaffCommentId implements Serializable {

    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false)
    @EqualsAndHashCode.Include
    private Comment commentId;

}