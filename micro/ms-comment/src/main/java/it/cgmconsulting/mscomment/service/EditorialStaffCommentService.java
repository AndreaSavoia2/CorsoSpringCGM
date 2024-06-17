package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.entity.EditorialStaffCommentId;
import it.cgmconsulting.mscomment.exception.GenericException;
import it.cgmconsulting.mscomment.exception.ResourceNotFoundException;
import it.cgmconsulting.mscomment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.mscomment.payload.response.EditorialStaffCommentResponse;
import it.cgmconsulting.mscomment.repository.EditorialStaffCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EditorialStaffCommentService {

    private final EditorialStaffCommentRepository escRepository;
    private final CommentService commentService;

    public ResponseEntity<?> createdEditorialStaffComment(EditorialStaffCommentRequest request) {
        Comment c = commentService.findById(request.getCommentId());

        EditorialStaffComment esc = new EditorialStaffComment(
                new EditorialStaffCommentId(c),
                request.getComment()
        );

        escRepository.save(esc);
        return ResponseEntity.status(201).body(EditorialStaffCommentResponse.fromEntityToResponse(esc));
    }

    @Transactional
    public ResponseEntity<?> updateEditorialStaffCommentBis(EditorialStaffCommentRequest request) {
        EditorialStaffComment escComment = escRepository.getById(request.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("EditorialStaffComment", "id", request.getCommentId()));
        escComment.setComment(request.getComment());
        escComment.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.status(200).body(EditorialStaffCommentResponse.fromEntityToResponse(escComment));
    }

    public ResponseEntity<?> updateEditorialStaffComment(EditorialStaffCommentRequest request){
        int result = escRepository.updateEditorialStaffComment(request.getComment(), request.getCommentId(), LocalDateTime.now());
        if (result == 0){
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", request.getCommentId());
        }
        return ResponseEntity.ok("Editorial staff Comment has been updated");
    }

    public ResponseEntity<?> deleteEditorialStaffComment(int commentId) {
        int result = escRepository.deleteEditorialStaffComment(commentId);
        if (result == 0){
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", commentId);
        }
        return ResponseEntity.ok("Editorial staff Comment has been deleted");
    }


}
