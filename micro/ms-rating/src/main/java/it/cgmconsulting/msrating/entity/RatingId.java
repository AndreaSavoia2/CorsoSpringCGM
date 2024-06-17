package it.cgmconsulting.msrating.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RatingId implements Serializable {

    private int postId;
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return postId == ratingId.postId && userId == ratingId.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
