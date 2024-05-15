package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final CommentService commentService;
    private final ReasonService reasonService;

    public String createReport(UserDetails userDetails, int commentId, String reason, LocalDate startDate){
        // istanziare un commento : verificare che il commento esiste, che non sia censurato e che non sia già stato segnalato
        Comment c = commentService.getCommentToReport(commentId);

        User reporter = (User) userDetails;
        if (c.getUserId().equals(reporter)){
            return "you cannot report youself";
        }

        // istanziare un oggetto reason : recuperarlo dal db
        Reason r = reasonService.findReasonById(new ReasonId(reason, startDate));

        // istanziare un oggetto reporting e salvarlo
        Reporting reporting = new Reporting(new ReportingId(c), r, reporter);

        reportingRepository.save(reporting);

        return "The comment " + c.getComment() + " has been reported";
    }

    /*@Transactional
    public String updateReport(String reason, LocalDate startDate, ReportingStatus status, int commentId) {
        // trovare il report e verificare che non sia già stato chiuso
        Comment comment = commentService.findCommentById(commentId);
        Reporting reporting = findById(new ReportingId(comment));
        //Reason r = reasonService.findReasonById(new ReasonId(reason, startDate));

        if (reporting.getStatus().name().startsWith("CLOSED")){
            return "the report i already in status 'CLOSED'";
        }
        else if (reporting.getStatus().equals(ReportingStatus.IN_PROGRESS) && status.equals(ReportingStatus.NEW)){
            return "Changing status not allowed";
        } else {
            if (status.equals(ReportingStatus.CLOSED_WITH_BAN)){
                comment.setCensored(true);
                comment.getUserId().setEnabled(false);
                //comment.getUserId().setBannedUntil(LocalDate.now().plusDays(r.getSeverity()));
                //reporting.setReason(r);
            }
            reporting.setStatus(status);
        }

        return null;
    }*/

    @Transactional
    public String updateReport(String reason, LocalDate startDate, ReportingStatus status, int commentId) {
        // trovare il report e verificare che non sia già stato chiuso
        Comment comment = commentService.findCommentById(commentId);
        Reporting reporting = findById(new ReportingId(comment));
        Reason r = reasonService.findReasonById(new ReasonId(reason, startDate));

        if (reporting.getStatus().name().startsWith("CLOSED")){
            return "the report i already in status 'CLOSED'";
        }
        else if (reporting.getStatus().equals(ReportingStatus.IN_PROGRESS) && status.equals(ReportingStatus.NEW)){
            return "Changing status not allowed";
        } else {

            if (status.equals(ReportingStatus.CLOSED_WITH_BAN)){

                if(comment.getUserId().isBanned()){
                    if(!comment.getUserId().getBannedUntil().isAfter(LocalDate.now().plusDays(r.getSeverity()))){
                        comment.getUserId().setBannedUntil(LocalDate.now().plusDays(r.getSeverity()));
                    }
                }else {
                    comment.getUserId().setEnabled(false);
                    comment.getUserId().setBannedUntil(LocalDate.now().plusDays(r.getSeverity()));
                }

                reporting.setReason(r);
                comment.setCensored(true);

            }
            reporting.setStatus(status);
        }

        return null;
    }

    public Reporting findById(ReportingId reportingId) {
        return reportingRepository.findById(reportingId).orElseThrow(
                () -> new ResourceNotFoundException("Reporting", "comment", reportingId.getCommentId().getId()));
    }

}

