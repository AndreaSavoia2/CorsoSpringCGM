package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.SectionTitle;
import it.cgmconsulting.mspost.service.SectionTitleService;
import jakarta.validation.constraints.Max;
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
public class SectionTitleController {

    private final SectionTitleService sectionTitleService;

    // Post -> titolo sezione in maiuscolo
    @PostMapping("/v1/section-titles")
    public ResponseEntity<?> addSectionTitle(@RequestParam @NotBlank @Size(max = 50, min = 2) String title){
        return sectionTitleService.addSectionTitle(title);
    }

    // get all -> solo visibili
    @GetMapping("/v2/section-titles")
    public ResponseEntity<?> getAllVisibleSectionTitle(){
        return sectionTitleService.getAllVisible();
    }

    @GetMapping("/v1/section-titles")
    public ResponseEntity<?> getAllSectionTitle(){
        return sectionTitleService.getAllSectionTitle();
    }

    // put -> Update sectionTitle
    @PutMapping("/v1/section-titles/{id}")
    public ResponseEntity<?> updateTitle(
            @PathVariable @Min(1) @Max(127) byte id,
            @RequestParam @NotBlank @Size(max = 50, min = 2) String mySectionTitle){
        return sectionTitleService.updateTitle(id, mySectionTitle);
    }

    // patch -> update visible
    @PatchMapping("/v1/section-titles/{id}")
    public ResponseEntity<?> switchVisibility(@PathVariable @Min(1) @Max(127) byte id){
        return sectionTitleService.switchVisibility(id);
    }
}
