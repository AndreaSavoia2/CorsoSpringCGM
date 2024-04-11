package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Short> {

    List<Tag> findByVisibleTrueOrderByTagName();
    List<Tag> findAllByOrderByTagName();
    List<Tag> findByVisibleFalseOrderByTagName();
}
