package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Section;
import it.cgmconsulting.mspost.entity.SectionTitle;
import it.cgmconsulting.mspost.payload.response.SectionTitleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionTitleRepository extends JpaRepository<SectionTitle,Byte> {
    boolean existsBySectionTitle(String sectionTitle);

    boolean existsBySectionTitleAndIdNot(String sectionTitle, byte id);

    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.SectionTitleResponse(" +
            "s.id, " +
            "sectionTitle) " +
            "FROM SectionTitle s WHERE visible")
    List<SectionTitleResponse> getAllVisible();
}
