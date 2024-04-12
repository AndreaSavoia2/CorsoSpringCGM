package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getTag(@PathVariable @Min(1) @Max(32767) short id){
        return new ResponseEntity<>(TAG_SERVICE.getTag(id),HttpStatus.OK);
    }

    @PostMapping
    public  ResponseEntity<?> createTag(
            @RequestParam
            @NotBlank(message = "The tag name contain at least one non whitespace character!")
            @Size(min = 4, max = 50) String tagName){
        Tag tag = TAG_SERVICE.createTag(tagName.toUpperCase().trim());
        if (tag != null){
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        }
        return  new ResponseEntity<>("The tag witch name " + tagName + " is alredy present", HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<?> updateTag(
            @RequestParam @NotBlank @Size(min = 4, max = 50) String tagName,
            @RequestParam @NotBlank @Size(min = 4, max = 50) String newTagName,
            @RequestParam boolean visible){
        Tag tag = TAG_SERVICE.updateTag(
                tagName.toUpperCase().trim(),
                newTagName.toUpperCase().trim(),
                visible);
        if (tag != null){
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        }
        return  new ResponseEntity<>("The tag witch name " + newTagName + " is alredy present", HttpStatus.CONFLICT);

    }

}
