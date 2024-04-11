package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tags")
public class TagController {

    public final TagService TAG_SERVICE;


    // Y = only visible
    // N = only not visibile
    // A = all
    @GetMapping
    public ResponseEntity<?> getAllTags(@RequestParam(defaultValue = "Y") Character visible){
        List<Tag> tags = TAG_SERVICE.getAllTags(visible);
        if(tags.isEmpty()){
            return new ResponseEntity<>(tags + " No tags found ", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping
    public  ResponseEntity<?> createTag(@RequestParam @NotBlank @Size(min = 4, max = 50) String tagName){
        return new ResponseEntity<>(TAG_SERVICE.createTag(tagName.toUpperCase()), HttpStatus.CREATED);
    }

}
