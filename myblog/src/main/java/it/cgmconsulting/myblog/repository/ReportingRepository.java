package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingDetailResponse;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {

    @Query(value = "Select new it.cgmconsulting.myblog.payload.response.ReportingResponse(" +
            "r.reportingId.commentId.id," +
            "r.status," +
            "r.reason.reasonId.reason," +
            "r.createdAt, " +
            "r.updateAt " +
            ")FROM Reporting r " +
            "WHERE r.status = :status")
    Page<ReportingResponse> getReporting(ReportingStatus status, Pageable pageable);

    @Query(value = "Select new it.cgmconsulting.myblog.payload.response.ReportingDetailResponse(" +
            "r.reportingId.commentId.id," +
            "r.status," +
            "r.reason.reasonId.reason," +
            "r.reportingId.commentId.comment, " +
            "r.reporter.username, " +
            "r.createdAt," +
            "r.updateAt" +
            ")FROM Reporting r " +
            "WHERE r.reportingId.commentId.id = :commentId")
    Optional<ReportingDetailResponse> getReportingDetail(int commentId);

}
