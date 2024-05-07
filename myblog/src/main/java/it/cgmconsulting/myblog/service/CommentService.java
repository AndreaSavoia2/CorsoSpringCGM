package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.request.CommentUpdateRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Value("${application.comment.time}")
    private int timeToUpdate;

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponse createComment(CommentRequest request, UserDetails userDetails){
        Post post = postService.findPostById(request.getPostId());
        Comment parent = null;
        if(request.getCommentId() != null){
            parent = findCommentById(request.getCommentId());
        }

        Comment comment = new Comment(
                request.getComment(),
                (User) userDetails,
                post,
                parent);
        commentRepository.save(comment);

        post.setTotComments((short) (post.getTotComments() + 1));

        return  new CommentResponse(comment.getId(),
                comment.getComment(),
                comment.getUserId().getUsername(),
                comment.getCreatedAt(),
                comment.getParent() != null ? comment.getParent().getId() : null);
    }

    public Comment findCommentById(int id){
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
    }

    @Transactional
    public String updateComment(CommentUpdateRequest request, UserDetails userDetails){
        Comment comment = findCommentById(request.getCommentId());
        // verifico autore del commento
        if(!((User) userDetails).equals(comment.getUserId())){
            return "you can update only your comments";
        }
        if(comment.isCensored()){
            return "you can update a censored comment";
        }
        // verifico tempo passato dalla creazione
        if(comment.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(timeToUpdate)) ){
            return  "you can update the comment only " + timeToUpdate + " sec after creation";
        }
        // set del nuovo commento
        comment.setComment(request.getComment());
        return "your comment has been update";
    }

    public String deleteComment(int id, UserDetails userDetails) {
        Comment comment = findCommentById(id);
        // verifico autore del commento
        if(!((User) userDetails).equals(comment.getUserId())){
            return "you can delete only your comments";
        }
        if(comment.isCensored()){
            return "you can delete a censored comment";
        }
        // verifico tempo passato dalla creazione
        if(comment.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(timeToUpdate)) ){
            return  "you can delete the comment only " + timeToUpdate + " sec after creation";
        }

        commentRepository.deleteById(id);
        return "your comment has been deleted";
    }

    /*public List<CommentResponse> getComments() {
        List<Comment> comments = commentRepository.getComments(LocalDateTime.now().minusSeconds(timeToUpdate));

        return comments.stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getContent(),
                        c.getUserId().getUsername(),
                        c.getCreatedAt(),
                        c.getParent() != null ? c.getParent().getId() : null))
                .collect(Collectors.toList());
    }*/

    public List<CommentResponse> getComments() {
        return commentRepository.getComments(LocalDateTime.now().minusSeconds(timeToUpdate));
    }

}
