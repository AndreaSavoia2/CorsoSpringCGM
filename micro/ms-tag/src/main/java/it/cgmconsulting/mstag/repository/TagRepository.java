package it.cgmconsulting.mstag.repository;

import it.cgmconsulting.mstag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    boolean existsByTagName(String tagName);

    boolean existsByTagNameAndIdNot(String tagName, int id);

    @Query("SELECT t.tagName FROM Tag t ORDER BY t.tagName")
    List<String>getAllTagName();

    @Query(value = "SELECT t FROM Tag t WHERE t.tagName IN (:tags)") // in quando si tratta di array
    Set<Tag> getTags(Set<String> tags);

}
