package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j  // fornisce un log
public class TagService {

    private final TagRepository TAG_REPOSITORY; // iniezione avventura con @RequiredArgsConstructor

    // lista di tag completa (visibili e no)
    public List<Tag> getAllTags(char visible){
        List<Tag> tags = new ArrayList<>();

        if(visible == 'A'){
            tags = TAG_REPOSITORY.findAllByOrderByTagName();
        } else if (visible == 'Y') {
            tags = TAG_REPOSITORY.findByVisibleTrueOrderByTagName();
        } else if (visible == 'N') {
            tags = TAG_REPOSITORY.findByVisibleFalseOrderByTagName();
        }

        log.info("Tag list contains " + tags.size()+ " elements");
        return tags;
    }

    //inserire un nuovo tag
    public Tag createTag(String tagName){
        return TAG_REPOSITORY.save(new Tag(tagName));
    }

    //modificare un tag esistente

    // cercare uno specifico tag
}
