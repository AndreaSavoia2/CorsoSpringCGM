package it.cgmconsulting.mscomment.controller;

import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v3")
    @CacheEvict(value = "tutti-i-commenti", key = "{#request.post}")
    public ResponseEntity<?> createComment(
            @RequestBody @Valid CommentRequest request,
            @RequestHeader("userId") int author
            ){
        return commentService.createComment(request, author);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getComment(@PathVariable @Min(1) int id){
        return commentService.getComment(id);
    }


    @GetMapping("/v1/bis/{id}")
    public ResponseEntity<?> getCommentBis(@PathVariable @Min(1) int id){
        return commentService.getCommentBis(id);
    }

    @Cacheable(value = "tutti-i-commenti", key = "{#postId}", unless = "#result == null")
    @GetMapping("/v0/{postId}")
    public ResponseEntity<?> getComments(@PathVariable @Min(1) int postId){
        return commentService.getComments(postId);
    }

    @Cacheable(value = "tutti-i-commenti", key = "{#postId}", unless = "#result == null")
    @GetMapping("/v0/full/{postId}")
    public ResponseEntity<?> getFullComments(@PathVariable @Min(1) int postId){
        log.info("STO ESEGUENDO IL METDO getFullComments #####");
        return commentService.getFullComments(postId);
    }


    // modifica del testo del comment possibile se effettuato entro 60 secondi dal primo salvataggio
    // ed effetuabile solo dallo user autore del commento
    @CacheEvict(value = "tutti-i-commenti", allEntries = true)
    @PatchMapping("/v3/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable @Min(1) int commentId,
                                           @RequestParam @NotBlank @Size(min = 1,max = 255) String comment,
                                           @RequestHeader("userId") int author){
        return commentService.updateComment(commentId, comment, author);
    }


    @DeleteMapping("/v3/{commentId}")
    @CacheEvict(value = "tutti-i-commenti", allEntries = true)
    public ResponseEntity<?> deleteComment(@PathVariable @Min(1) int commentId,
                                           @RequestHeader("userId") int author){
        return commentService.deleteComment(commentId, author);
    }

}
