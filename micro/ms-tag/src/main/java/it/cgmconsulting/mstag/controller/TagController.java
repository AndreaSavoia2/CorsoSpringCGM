package it.cgmconsulting.mstag.controller;

import it.cgmconsulting.mstag.service.TagService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService tagService;

    @PostMapping("/v1")
    public ResponseEntity<?> createTag(@RequestParam @NotBlank @Size(max=20) String tagName){
        return tagService.createTag(tagName.toUpperCase());
    }

    @GetMapping("/v0")
    public ResponseEntity<?> getAllTags(){
        return tagService.getAllTags();
    }

    @PatchMapping("/v1/{id}")
    public ResponseEntity<?> updateTag(
            @PathVariable @Min(1) int id,
            @RequestParam @NotBlank @Size(max=20) String newTagName
    ){
        return tagService.updateTag(id,newTagName.toUpperCase().trim());
    }

    @DeleteMapping("/v1{id}")
    public ResponseEntity<?> deleteTag(@PathVariable @Min(1) int id){
        return tagService.deleteTag(id);
    }

}
