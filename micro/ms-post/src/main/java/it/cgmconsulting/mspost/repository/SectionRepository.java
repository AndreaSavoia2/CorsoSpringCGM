package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Section;
import it.cgmconsulting.mspost.payload.response.SectionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query(value = "SELECT new  it.cgmconsulting.mspost.payload.response.SectionResponse(" +
            "s.id," +
            "s.post.id," +
            "s.title.sectionTitle," +
            "s.subTitle," +
            "s.content," +
            "s.sectionImage)" +
            "FROM Section s " +
            "WHERE s.post.id = :postId")
    Set<SectionResponse> getSectionsResponse(int postId);

}
