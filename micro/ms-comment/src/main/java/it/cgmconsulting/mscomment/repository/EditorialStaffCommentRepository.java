package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.entity.EditorialStaffCommentId;
import it.cgmconsulting.mscomment.payload.response.EditorialStaffCommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EditorialStaffCommentRepository extends JpaRepository<EditorialStaffComment, EditorialStaffCommentId> {

    @Query(value = "SELECT * FROM editorial_staff_comment esc WHERE esc.comment_id = :id" , nativeQuery = true)
    Optional<EditorialStaffComment> getById(int id);

    @Query(value = "SELECT esc FROM EditorialStaffComment esc WHERE esc.id.commentId.id = :id")
    Optional<EditorialStaffComment> getByIdJpql(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE editorial_staff_comment esc " +
            "SET esc.comment = :comment, " +
            "esc.updated_at = :now " +
            "where esc.comment_id = :id", nativeQuery = true)
    int updateEditorialStaffComment(String comment, int id , LocalDateTime now);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM editorial_staff_comment esc " +
            "WHERE esc.comment_id = :id", nativeQuery = true)
    int deleteEditorialStaffComment(int id);

    /*@Modifying
    @Transactional
    @Query(value = "DELETE FROM EditorialStaffComment esc WHERE esc.id.commentId.id = :commentId")
    int deleteEditorialStaffCommentJpql(int id);*/

    @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.EditorialStaffCommentResponse(" +
            "esc.id.commentId.id, " +
            "esc.comment, " +
            "CASE WHEN esc.updatedAt IS NULL THEN esc.createdAt ELSE esc.updatedAt END" +
            ") FROM EditorialStaffComment esc " +
            "WHERE esc.id.commentId.post = :postId")
    List<EditorialStaffCommentResponse> getEscs(int postId);
}
