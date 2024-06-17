package it.cgmconsulting.msrating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private byte rate;

    public Rating(RatingId ratingId, byte rate) {
        this.ratingId = ratingId;
        this.rate = rate;
        this.createdAt = LocalDateTime.now();
    }
}
