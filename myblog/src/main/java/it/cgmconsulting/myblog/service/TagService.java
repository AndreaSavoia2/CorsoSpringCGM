package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    //cercare uno specifico tag
    public Tag getTag(short id){
        return TAG_REPOSITORY.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tag", "Id", id));
    }

    //inserire un nuovo tag
    public Tag createTag(String tagName){
        //controllo che il valore già non esisti
        if (TAG_REPOSITORY.existsByTagName(tagName)){
            return null;
        }
        return TAG_REPOSITORY.save(new Tag(tagName));
    }

    //modificare un tag esistente
    @Transactional
    public Tag updateTag(String tagName, String newTagName, boolean visible){
        // trovo tag da modifcare
        Tag tag = TAG_REPOSITORY.findByTagName(tagName).
                orElseThrow(() -> new ResourceNotFoundException("Tag", "tagName", tagName));
        // verifico che non esista un altro recond che abbia lo stesso tang name di quello nuovo
        if(TAG_REPOSITORY.existsByTagNameAndIdNot(newTagName, tag.getId())){
            return null;
        }
        tag.setTagName(newTagName);
        tag.setVisible(visible);
        // con @Transactional si occupa lui di salvare solo se tutte le operazioni vanno a buon fine
        // in caso contrario tutte le operazioni vengono annullate
        // funziona solo su elementi già esistenti
        return tag;
    }

}
