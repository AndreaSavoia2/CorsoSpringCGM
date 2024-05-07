package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    /*@Query("select c from Comment c WHERE c.censored IS false AND c.updateAt <= :now order by c.updateAt DESC")
    List<Comment> getComments(LocalDateTime now);*/

    @Query("select new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "c.userId.username, " +
            "c.updateAt, " +
            "c.parent.id " +
            ") from Comment c " +
            "WHERE c.censored = false AND c.updateAt <= :now order by c.updateAt DESC ")
    List<CommentResponse> getComments(LocalDateTime now);
}
