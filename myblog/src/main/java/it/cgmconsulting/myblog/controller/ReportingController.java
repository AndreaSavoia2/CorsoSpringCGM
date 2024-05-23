package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingDetailResponse;
import it.cgmconsulting.myblog.service.ReasonService;
import it.cgmconsulting.myblog.service.ReportingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Valid
public class ReportingController {

    private final ReportingService reportingService;

    @PostMapping("/v1/reporting")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<String> createReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @Min(1) int commentId,
            @RequestParam @NotBlank @Size(min = 3 , max = 30) String reason,
            @RequestParam @NotNull LocalDate startDate){
            return new ResponseEntity<String>(reportingService.createReport(userDetails, commentId,reason,startDate), HttpStatus.OK);
    }

    @PutMapping("/v1/reporting")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateReport(
            @RequestParam @NotBlank @Size(min = 3 , max = 30) String reason,
            @RequestParam @NotNull LocalDate startDate,
            @RequestParam ReportingStatus status,
            @RequestParam @Min(1) int commentId
            ){
        return new ResponseEntity<>(reportingService.updateReport(reason,startDate,status,commentId),HttpStatus.OK);
    }

    @GetMapping("/v1/reporting")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReport(
            @RequestParam(defaultValue = "0") int pageNumber, //numero di pagina da partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "createdAt") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam ReportingStatus status
    ){
        return new ResponseEntity<>(reportingService.getReports(pageNumber, pageSize, sortBy, direction, status), HttpStatus.OK);
        //return ResponseEntity.ok(reportingService.getReports(pageNumber, pageSize, sortBy, direction, status));
    }

    @GetMapping("/v1/reporting/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReportDetail(@PathVariable @Min(1) int commentId
    ){
        return new ResponseEntity<>( reportingService.getReportingDetail(commentId), HttpStatus.OK);
    }
}
