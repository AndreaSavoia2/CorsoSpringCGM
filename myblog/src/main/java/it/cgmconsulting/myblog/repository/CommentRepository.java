package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("select c from Comment c WHERE c.censored IS false AND c.updateAt <= :now order by c.updateAt DESC")
    List<Comment> getComments(LocalDateTime now);

    @Query("select new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
            "c.id, " +
            "c.content, " +
            "c.userId.username, " +
            "c.updateAt, " +
            "(case when c.parent is null then null else c.parent.id end) " +
            ") from Comment c " +
            "WHERE c.censored = false AND c.updateAt <= :now order by c.updateAt DESC ")
    List<CommentResponse> getComments1(LocalDateTime now);
}
