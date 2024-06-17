package it.cgmconsulting.mstag.service;


import it.cgmconsulting.mstag.entity.Tag;
import it.cgmconsulting.mstag.exception.GenericException;
import it.cgmconsulting.mstag.exception.ResourceNotFoundException;
import it.cgmconsulting.mstag.repository.PostTagRepository;
import it.cgmconsulting.mstag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;


    public ResponseEntity<?> createTag(String tagName) {

        if(tagRepository.existsByTagName(tagName)){
            throw new GenericException("Tag name already exists", HttpStatus.CONFLICT);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagRepository.save(new Tag(tagName)));
    }

    public ResponseEntity<?> getAllTags() {
        return ResponseEntity.ok(tagRepository.getAllTagName());
    }

    @Transactional
    public ResponseEntity<?> updateTag(int id, String newTagName) {

        if(tagRepository.existsByTagNameAndIdNot(newTagName, id))
            throw new GenericException("Tag name already exists", HttpStatus.CONFLICT);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Tag","id",id));

        tag.setTagName(newTagName);
        return ResponseEntity.ok(tag);
    }

    @Transactional
    public ResponseEntity<?> deleteTag(int id) {
        tagRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
