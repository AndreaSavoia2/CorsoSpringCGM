package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.SectionTitle;
import it.cgmconsulting.mspost.exception.GenericException;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.repository.SectionTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionTitleService {

    private final SectionTitleRepository sectionTitleRepository;

    public ResponseEntity<?> addSectionTitle(String title){
        if (sectionTitleRepository.existsBySectionTitle(title.toUpperCase())){
            throw new GenericException("This section title already exists",HttpStatus.CONFLICT);
            //return ResponseEntity.status(HttpStatus.CONFLICT).body("Titolo già esistente");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionTitleRepository
                        .save(new SectionTitle(title.toUpperCase())));
    }

    public ResponseEntity<?> getAllVisible(){
        return ResponseEntity.status(HttpStatus.OK).body(sectionTitleRepository.getAllVisible());
    }

    public ResponseEntity<?> getAllSectionTitle(){
        return ResponseEntity.status(HttpStatus.OK).body(sectionTitleRepository.findAll());
    }

    @Transactional
    public ResponseEntity<?> updateTitle(byte id, String mySectionTitle) {
        if (sectionTitleRepository.existsBySectionTitleAndIdNot(mySectionTitle, id)){
            throw new GenericException("This section title already exists",HttpStatus.CONFLICT);
        }
        SectionTitle sectionTitle = findById(id);
        sectionTitle.setSectionTitle(mySectionTitle.toUpperCase());
        return ResponseEntity.ok(sectionTitle);
    }

    protected SectionTitle findById(byte id){
        return sectionTitleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Section title", "id", id));
    }

    @Transactional
    public ResponseEntity<?> switchVisibility(byte id) {
        SectionTitle sectionTitle = findById(id);
        sectionTitle.setVisible(!sectionTitle.isVisible());
        return ResponseEntity.ok(sectionTitle);
    }
}
