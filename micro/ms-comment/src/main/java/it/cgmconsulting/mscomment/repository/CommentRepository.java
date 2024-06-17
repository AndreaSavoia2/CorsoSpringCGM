package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.response.CommentFullResponseBis;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "CAST(c.author AS string), " +
            "CASE WHEN c.updatedAt IS NULL THEN c.createdAt ELSE c.updatedAt END " +
            ") FROM Comment c " +
            "WHERE c.id = :id")
    Optional<CommentResponse> getComment(int id);

    @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.CommentResponse(" +
            "c.id, " +
            "CASE WHEN c.censored = false THEN c.comment else '*******' END , " +
            "CAST(c.author AS string)," +
            "CASE WHEN c.updatedAt IS NULL THEN c.createdAt ELSE c.updatedAt END " +
            ") FROM Comment c " +
            "WHERE c.post = :postId")
    List<CommentResponse> getComments(int postId);

    @Query(value = "SELECT c.author FROM Comment c")
    Set<Integer> getAuthorIds();

    @Query(value="SELECT new it.cgmconsulting.mscomment.payload.response.CommentFullResponseBis(" +
            "c.id, " +
            "CAST(c.author AS string), " +
            "CASE WHEN c.censored = false THEN c.comment ELSE '********' END AS comment," +
            "CASE WHEN c.updatedAt IS NULL THEN c.createdAt ELSE c.updatedAt END AS date," +
            "CASE WHEN esc.comment IS NULL THEN NULL ELSE 'REDAZIONE' END AS escAuthor," +
            "esc.comment AS esc_comment, " +
            "CASE WHEN esc.updatedAt IS NULL THEN esc.createdAt ELSE esc.updatedAt END AS esc_date" +
            ") FROM Comment c " +
            "LEFT JOIN EditorialStaffComment esc ON esc.id.commentId.id = c.id " +
            "WHERE c.post = :postId " +
            "ORDER BY date DESC")
    List<CommentFullResponseBis> getFullComments(int postId);

}
