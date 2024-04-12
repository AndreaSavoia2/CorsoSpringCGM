package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Short> {

    List<Tag> findByVisibleTrueOrderByTagName();
    List<Tag> findAllByOrderByTagName();
    List<Tag> findByVisibleFalseOrderByTagName();
    Optional<Tag> findByTagName(String tagName);
    boolean existsByTagName(String tagName);
    boolean existsByTagNameAndIdNot(String tagName, short id);
}
