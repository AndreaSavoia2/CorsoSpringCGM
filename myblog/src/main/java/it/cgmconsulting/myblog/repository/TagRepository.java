package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag,Short> {

    @Query(value = "SELECT t.tag_name from post_tags pt " +
            "inner join tag t on pt.tag_id = t.id " +
            "and pt.post_id = :idPost", nativeQuery = true)
    Set<String> getTagNamesByPost(int idPost);
    List<Tag> findByVisibleTrueOrderByTagName();
    List<Tag> findAllByOrderByTagName();
    List<Tag> findByVisibleFalseOrderByTagName();
    Set<Tag> findAllByVisibleTrueAndTagNameIn(Set<String> tagNames);
    Optional<Tag> findByTagName(String tagName);
    boolean existsByTagName(String tagName);
    boolean existsByTagNameAndIdNot(String tagName, short id);
}
